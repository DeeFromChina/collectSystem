package com.golead.disService.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.disService.common.utils.DataName;

public class ExecuteLogFile extends Thread {

   private static Logger             logger             = Logger.getLogger(ExecuteLogFile.class);
   private Connection                connection;
   private ExecuteLogFileCallback    executeLogFileCallback;
   private List<Map<String, Object>> dataList           = new ArrayList<Map<String, Object>>();
   private String[]                  tableNames         = null;
   private Map<String, Integer[]>    typeMap            = null;
   private Map<String, String>       columsMap          = null;
   private Map<String, Object>       params             = null;
   private int                       DATATYPE_NUMBER    = 2;
   private int                       DATATYPE_TIMESTAMP = 93;
   private int                       DATATYPE_DATE      = 91;

   public ExecuteLogFile(List<Map<String, Object>> list, String[] tableNames, Map<String, Object> map) {
      this.dataList = list;
      this.tableNames = tableNames;
      this.typeMap = (Map<String, Integer[]>) map.get("type");
      this.columsMap = (Map<String, String>) map.get("columsMap");
      this.params = map;
   }

   public ExecuteLogFileCallback getExecuteLogFileCallback() {
      return executeLogFileCallback;
   }

   public void setExecuteLogFileCallback(ExecuteLogFileCallback executeLogFileCallback) {
      this.executeLogFileCallback = executeLogFileCallback;
   }

   @Override
   public void run() {
      try {
         connection = DataSource.getConnection();
      }
      catch (SQLException e) {
         logger.error("数据库连接失败。");
         return;
      }

      try {
         connection.setAutoCommit(false);
      }
      catch (SQLException e2) {
         logger.error("数据库连接设置手动提交失败。");
         return;
      }

      readFile();

      try {
         connection.setAutoCommit(true);
      }
      catch (SQLException e) {
         logger.error("数据库连接设置自动提交失败。");
      }
      try {
         connection.close();
      }
      catch (SQLException e) {
         logger.error("数据库关闭失败。");
      }
   }

   private void readFile() {
      Map<String, Integer> pkId = new HashMap<String, Integer>();
      for (int i = 0; i < tableNames.length; i++) {
         int id = checkPK(tableNames[i]);
         pkId.put(tableNames[i], id);
      }

      if (pkId == null) { return; }
      List<String> sqlList = new ArrayList<String>();
      int cnt = 0;
      for (Map<String, Object> dataMap : dataList) {
         String tableName;
         StringBuffer values = new StringBuffer();
         if (dataMap.size() > 14) {
            tableName = tableNames[1];
         }
         else {
            tableName = tableNames[0];
         }
         String colum = columsMap.get(tableName);
         String[] colums = colum.split(",");
         Integer[] type = typeMap.get(tableName);
         for (int i = 0; i < type.length; i++) {
            if(values.length() > 0){
               values.append(",");
            }
            if (type[i] == DATATYPE_NUMBER) {
               values.append(dataMap.get(colums[i]));
            }
            else if (type[i] == DATATYPE_TIMESTAMP || type[i] == DATATYPE_DATE) {
               String data = dataMap.get(colums[i]).toString();
               int d = dataMap.get(colums[i]).toString().indexOf(".");
               if (d > -1) values.append("to_date('" + data.substring(0, d) + "','yyyy-mm-dd hh24:mi:ss')");
               else values.append("to_date('" + data + "','yyyy-mm-dd hh24:mi:ss')");
            }
            else {
               String str;
               try {
                  str = new String(dataMap.get(colums[i]).toString().trim().getBytes("ISO8859-1"),"UTF-8");
                  values.append("'" + str + "'");
               }
               catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
               }
            }
         }
         String where = " WHERE LOG_TIME = to_date('" + dataMap.get("time") + "','yyyy-MM-dd hh24:mi:ss')"
               + " AND COMPANY = '" + params.get("COMPANY") + "' " + " AND STATION = '"
               + params.get("STATION") + "' " + " AND MSG_ID = '" + dataMap.get("msgID") + "' ";
         colum = colum.replace(DataName.MsgID, "MSG_ID");
         colum = colum.replace(DataName.SOURCE, "SOURCE");
         colum = colum.replace(DataName.LOG_TIME, "LOG_TIME");
         sqlList.add(String.format("delete %s %s", tableName, where));
         sqlList.add(String.format("insert into %s(ID,%s) values(%s,%s)", tableName, colum, Integer.valueOf(pkId.get(tableName)) + 1, values.toString()));
         pkId.put(tableName, Integer.valueOf(pkId.get(tableName)) + 1);
         cnt++;
         if (cnt % 500 == 0 && cnt > 0) {
            execTransaction(sqlList, cnt);
            sqlList.clear();
         }
      }
      if (sqlList.size() > 0) {
         execTransaction(sqlList, cnt);
         sqlList.clear();
      }
   }

   private Integer checkPK(String tableName) {
      Integer res = null;
      ResultSet rs = null;
      Statement stmt = null;
      try {
         stmt = connection.createStatement();
         String sql = "select max(ID) as maxValue from " + tableName;
         rs = stmt.executeQuery(sql);
         if (rs == null) {
            res = 0;
         }
         else if (rs.next()) {
            int pk = rs.getInt("maxValue");
            if (rs.wasNull()) res = 0;
            else res = pk;
         }
         else res = 0;
      }
      catch (SQLException e) {
         logger.error("查询数据表记录数错误：" + tableName);
         res = 0;
      }
      finally {
         if (rs != null) {
            try {
               rs.close();
               rs = null;
            }
            catch (SQLException e) {
               logger.error("查询数据表记录数错误：" + tableName + "表关闭错误。");
            }
         }
      }
      if (stmt != null) {
         try {
            stmt.close();
            stmt = null;
         }
         catch (SQLException e) {
            logger.error("查询数据表记录数错误：" + tableName + "表关闭错误。");
         }
      }
      return res;
   }

   private boolean execTransaction(List<String> sqlList, int count) {
      boolean res;
      Statement stmt = null;
      String tableName = "";
      List<String> list = new ArrayList<String>();
      try {
         stmt = connection.createStatement();
         for (int i = 0; i < sqlList.size(); i++) {
            String sql = sqlList.get(i).toString();
            if (sql.indexOf(tableNames[0]) > -1) {
               tableName = tableNames[0];
            }
            else if (sql.indexOf(tableNames[1]) > -1) {
               tableName = tableNames[1];
            }
            stmt.addBatch(sql);
         }
         stmt.executeBatch();
         connection.commit();
         stmt.clearBatch();
         logger.info(tableName + "数据插入成功，第" + count + "行。");
         list.clear();
         res = true;
      }
      catch (Exception e) {
         e.printStackTrace();
         logger.error(tableName + "(" + count + ")数据插入错误：" + e.getMessage());
         try {
            connection.rollback();
         }
         catch (SQLException e1) {
            logger.error(tableName + "数据回滚失败。");
         }
         res = false;
      }
      finally {
         try {
            if (stmt != null) {
               stmt.close();
               stmt = null;
            }
         }
         catch (SQLException e) {
            logger.error("数据库语句清理失败。");
         }
      }
      return res;
   }

   public class ExecuteLogFileCallback {

   }
}
