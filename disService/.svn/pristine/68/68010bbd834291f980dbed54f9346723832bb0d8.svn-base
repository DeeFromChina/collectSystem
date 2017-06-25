package com.golead.disService.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.disService.callback.ExecuteFileCallback;

public class ExecuteFile extends Thread {
   private static Logger       logger             = Logger.getLogger(ExecuteFile.class);
   private Connection          connection;
   private ExecuteFileCallback  executeFileCallback;

   private File                file;
   private String              tableName;

   private int                 DATATYPE_NUMBER    = 2;
   private int                 DATATYPE_TIMESTAMP = 93;
   private int                 DATATYPE_DATE      = 91;
   private Map<String, Object> parameters;

   public ExecuteFile(File file, String tableName, Map<String, Object> parameters) {
      this.file = file;
      this.tableName = tableName;
      this.parameters = parameters;
   }

   public ExecuteFileCallback getExecuteFileCallback() {
      return executeFileCallback;
   }

   public void setExecuteFileCallback(ExecuteFileCallback executeFileCallback) {
      this.executeFileCallback = executeFileCallback;
   }

   @Override
   public void run() {
      try {
         connection = DataSource.getConnection();
      } catch (SQLException e) {
         logger.error("数据库连接失败。");
         executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
         return;
      }

      try {
         connection.setAutoCommit(false);
      } catch (SQLException e2) {
         logger.error("数据库连接设置手动提交失败。");
         executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
         return;
      }

      readFile();

      try {
         connection.setAutoCommit(true);
      } catch (SQLException e) {
         logger.error("数据库连接设置自动提交失败。");
      }
      try {
         connection.close();
      } catch (SQLException e) {
         logger.error("数据库关闭失败。");
      }
   }

   private void readFile() {
      String stationId = parameters.get("stationId").toString();
      String catalogName = parameters.get("catalogName").toString();
      String primaryKey = parameters.get("primaryKey").toString();
      String columns = parameters.get("columns").toString();
      Integer[] types = (Integer[]) parameters.get("types");

      BufferedReader br = null;
      try {
         br = new BufferedReader(new FileReader(file));
      } catch (FileNotFoundException e) {
         logger.error(String.format("加油站【%s】文件【%s】数据文件【%s】打不开。", stationId, catalogName, file.getName()));
         executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
         return;
      }

      String[] colArray = columns.trim().split(",");
      String[] pkArray = primaryKey.trim().split(";");
      Integer[] indexs = new Integer[pkArray.length];
      Integer maxValue = null;
      if (pkArray.length == 1) {
         // 对于单字段、自增长整形数主键，获取主键最大值，只对checkPK方法中指定的表有效。
         maxValue = checkPK(tableName, primaryKey, stationId);
      }

      for (int j = 0; j < pkArray.length; j++) {
         String pk = pkArray[j];
         if (pk.equals("")) continue;
         for (int i = 0; i < colArray.length; i++) {
            if (pk.equalsIgnoreCase(colArray[i].trim())) {
               indexs[j] = i;
               break;
            }
         }
      }
      for (Integer idx : indexs) {
         if (idx == null) {
            logger.error(String.format("加油站【%s】文件【%s】主键设置错误：%s。", stationId, catalogName, tableName));
            executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
            return;
         }
      }

      StringBuffer sb = new StringBuffer("");
      for (int j = 0; j < pkArray.length; j++) {
         if (j == 0) sb.append(" where ");
         else sb.append(" and ");
         sb.append(pkArray[j]).append("=%s ");
      }
      String whereStr = sb.toString();

      List<String> sqlList = new ArrayList<String>();
      String in = null;
      int cnt = 0;
      try {
         while ((in = br.readLine()) != null) {
            String[] valArray = in.split(";", -1);
            String[] values = new String[pkArray.length + 1];
            for (int ii = 0; ii < indexs.length; ii++) {
               String v = valArray[indexs[ii]];
               values[ii] = v.trim().equals("null") ? "null" : String.format("'%s'", v);
            }

            StringBuffer vals = new StringBuffer("");
            for (int l = 0; l < valArray.length; l++) {
               if (l > 0) vals.append(",");
               if ("null".equalsIgnoreCase(valArray[l])) {
                  vals.append("null");
                  continue;
               }
               if (types[l] == DATATYPE_NUMBER) {
                  vals.append(valArray[l].trim());
               }
               else if (types[l] == DATATYPE_TIMESTAMP || types[l] == DATATYPE_DATE) {
                  int d = valArray[l].trim().indexOf(".");
                  if (d > -1) vals.append("to_date('" + valArray[l].trim().substring(0, d) + "','yyyy-mm-dd hh24:mi:ss')");
                  else vals.append("to_date('" + valArray[l].trim() + "','yyyy-mm-dd hh24:mi:ss')");
               }
               else {
                  vals.append("'" + valArray[l].trim() + "'");
               }
            }
            if (maxValue == null) {
               sqlList.add("delete BOS_" + tableName + String.format(whereStr, values) + " and STATION_CODE='" + stationId + "'");
               sqlList.add(String.format("insert into BOS_%s(%s,STATION_CODE) values(%s,'%s')", tableName, columns, vals.toString(), stationId));
               cnt++;
            }
            else {
               String v = values[0];
               if (v != null) {
                  try {
                     Integer va = Integer.valueOf(v.replaceAll("'", ""));
                     if (va > maxValue) {
                        sqlList.add(String.format("insert into BOS_%s(%s,STATION_CODE) values(%s,'%s')", tableName, columns, vals.toString(), stationId));
                        cnt++;
                     }
                  } catch (NumberFormatException ne) {
                     ne.printStackTrace();
                  }
               }
            }
            if (cnt % 1000 == 0 && cnt > 0) {
               boolean res = execTransaction(sqlList, cnt);
               if (res) executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_SUCESS, null);
               else {
                  executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
                  return;
               }
               sqlList.clear();
            }
         }
         if (sqlList.size() > 0) {
            boolean res = execTransaction(sqlList, cnt);
            if (res) executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_SUCESS, null);
            else {
               executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
               return;
            }
         }
      } catch (IOException e) {
         logger.error(String.format("加油站【%s】文件【%s】数据文件【%s】读取失败。", stationId, catalogName, file.getName()));
         executeFileCallback.processCallback(ExecuteFileCallback.CODE_PROCESS_FAILUE, null);
      } finally {
         try {
            br.close();
         } catch (IOException e) {
            logger.error(String.format("加油站【%s】文件【%s】数据文件【%s】关闭失败。", stationId, catalogName, file.getName()));
         }
      }

   }

   private String[] tables = { "item" };

   private Integer checkPK(String tableName, String pkField, String stationCode) {
      String str = null;
      for (String t : tables) {
         if (t.equalsIgnoreCase(tableName)) {
            str = tableName;
            break;
         }
      }
      if (str == null) return null;

      Integer res = null;
      ResultSet rs = null;
      Statement stmt = null;
      try {
         stmt = connection.createStatement();
         String sql = "select max(" + pkField + ") as maxValue from bos_" + tableName + " where station_code='" + stationCode + "'";
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
      } catch (SQLException e) {
         logger.error("查询数据表记录数错误：" + tableName);
         res = 0;
      } finally {
         if (rs != null) {
            try {
               rs.close();
               rs = null;
            } catch (SQLException e) {
               logger.error("查询数据表记录数错误：" + tableName + "表关闭错误。");
            }
         }
         if (stmt != null) {
            try {
               stmt.close();
               stmt = null;
            } catch (SQLException e) {
               logger.error("查询数据表记录数错误：" + tableName + "表关闭错误。");
            }
         }
      }
      return res;
   }

   private boolean execTransaction(List<String> sqlList, int count) {
      boolean res;
      Statement stmt = null;
      try {
         stmt = connection.createStatement();
         for (int i = 0; i < sqlList.size(); i++) {
            stmt.addBatch(sqlList.get(i).toString());
         }
         stmt.executeBatch();
         connection.commit();
         stmt.clearBatch();
         logger.info(tableName + "数据插入成功，第" + count + "行。");
         res = true;
      } catch (Exception e) {
         e.printStackTrace();
         logger.error(tableName + "(" + count  + ")数据插入错误：" + e.getMessage());
         try {
            connection.rollback();
         } catch (SQLException e1) {
            logger.error(tableName + "数据回滚失败。");
         }
         res = false;
      } finally {
         try {
            if (stmt != null) {
               stmt.close();
               stmt = null;
            }
         } catch (SQLException e) {
            logger.error("数据库语句清理失败。");
         }
      }
      return res;
   }

   public String getTableName() {
      return tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }
}
