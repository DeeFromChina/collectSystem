package com.golead.dasService.collection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.golead.dasService.common.Constant;
import com.golead.dasService.common.utils.DateUtil;
import com.golead.dasService.common.utils.FileUtil;

public abstract class CollectionBase implements CollectionInterface {

   private static Logger    logger         = Logger.getLogger(CollectionBase.class);

   private SimpleDateFormat outFormat      = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

   private SimpleDateFormat inFormat       = new SimpleDateFormat("yyyyMMddHHmmss");

   protected String         catalogName;

   protected Properties     properties;

   protected String         fileName;

   protected String         dataName;

   protected String         tableName;

   protected String[]       columnNames;

   protected String         primaryKey;

   protected Connection     connection;

   private String           stationId;

   private String           stationName;

   public void init(String catalogName, Connection connection, Properties properties) {
      this.catalogName = catalogName;
      this.connection = connection;
      this.properties = properties;
      this.stationId = properties.getProperty("company.stationID");
      this.stationName = properties.getProperty("company.stationName");
   }

   public String formatDate(Date date) throws ParseException {
      if (date == null) return null;
      return outFormat.format(date);
   }

   public String dateOnlyFormat(Date date) throws ParseException {
      if (date == null) return null;
      return onlyDateFormat.format(date);
   }

   public String formatDate(String inStr) throws ParseException {
      Date date = inFormat.parse(inStr);
      return outFormat.format(date);
   }

   public abstract String prepareSql(Map<String, Object> parameters);

   private String[] getColumns(ResultSetMetaData rsmd) throws SQLException {
      String[] columns = new String[rsmd.getColumnCount()];
      for (int i = 1; i <= rsmd.getColumnCount(); i++) {
         columns[i - 1] = rsmd.getColumnLabel(i);
      }
      return columns;
   }

   @Override
   public boolean goCollection(Map<String, Object> parameters) {
      Statement stmt = null;
      ResultSet rs = null;
      BufferedWriter writer = null;

      String sql = prepareSql(parameters);
      if (sql == null) return false;
      String fName = getFilePath() + this.fileName;

      try {
         logger.info("执行查询语句：" + sql);
         stmt = connection.createStatement();
         rs = stmt.executeQuery(sql);
         if (rs == null) return false;

         ResultSetMetaData meta = rs.getMetaData();
         // this.tableName = meta.getTableName(1);
         this.columnNames = getColumns(meta);

         writer = new BufferedWriter(new FileWriter(new File(fName + ".dat")));
         logger.info("生成数据文件：" + fName + ".dat");

         while (rs.next()) {
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < this.columnNames.length; i++) {
               sb.append((i > 0 ? ";" : ""));

               String columnName = this.columnNames[i];

               Object obj = rs.getObject(columnName);
               if (!(obj instanceof String)) {
                  sb.append(String.valueOf(obj));
               }
               else {
                  byte[] bytes = rs.getBytes(columnName);
                  sb.append(bytes != null ? new String(bytes, "GBK").replaceAll("'", "") : "null");
               }
            }
            writer.write(sb.toString());
            writer.newLine();
         }
         writer.flush();

      } catch (UnsupportedEncodingException e) {
         logger.error("数据编码错误。");
         logger.error(e);
         return false;

      } catch (SQLException e) {
         logger.error("数据查询错误。");
         logger.error(e);
         return false;

      } catch (IOException e) {
         logger.error("数据文件写错误。");
         logger.error(e);
         return false;
      } catch (Exception e) {
         logger.error("系统错误：" + e.getMessage());
         return false;
      } finally {
         colseObject(stmt, rs, writer);
      }

      generateCfg();
      return true;
   }

   protected void colseObject(Statement stmt, ResultSet rs, BufferedWriter writer) {
      if (rs != null) {
         try {
            rs.close();
            rs = null;
         } catch (SQLException e) {
            logger.error("数据集操作错误。");
         }
      }
      if (stmt != null) {
         try {
            stmt.close();
            stmt = null;
         } catch (SQLException e) {
            logger.error("数据集操作错误。");
         }
      }
      if (writer != null) {
         try {
            writer.close();
            writer = null;
         } catch (IOException e) {
            logger.error("数据文件操作错误。");
         }
      }
   }

   protected void generateCfg() {
      Properties cfg = FileUtil.loadDataCfg(this.catalogName);
      String tabs = cfg.getProperty("catalog.tables");
      if (tabs == null) tabs = "";

      StringBuffer columns = new StringBuffer();
      for (int i = 0; i < this.columnNames.length; i++) {
         columns.append(i > 0 ? "," : "");
         columns.append(this.columnNames[i]);
      }

      cfg.setProperty("catalog.tables", tabs += this.tableName + ";");
      cfg.setProperty("data." + this.tableName + ".createTime", DateUtil.lFormat(new Date()));
      cfg.setProperty("data." + this.tableName + ".status", Constant.STATUS_COLLECTED);
      cfg.setProperty("data." + this.tableName + ".dataName", this.dataName);
      cfg.setProperty("data." + this.tableName + ".fileName", this.fileName + ".dat");
      cfg.setProperty("data." + this.tableName + ".columns", columns.toString());
      cfg.setProperty("data." + this.tableName + ".primaryKey", this.primaryKey);

      FileUtil.saveDataCfg(this.catalogName, cfg);
      logger.info("更新数据配置文件：" + Constant.DIR_DAT + this.catalogName + Constant.SFX_CFG);
   }

   public Properties loadProperties() {
      Properties prop = new Properties();
      String fName = Constant.DIR_DAT + fileName + ".properties";
      try {
         InputStream in = new FileInputStream(fName);
         prop.load(in);
      } catch (IOException e) {
         logger.error("读取配置文件" + fName + "错误。");
      }
      return prop;
   }

   public void saveProperties(Properties props) {
      FileOutputStream oFile = null;
      String fName = Constant.DIR_DAT + fileName + ".properties";
      try {
         oFile = new FileOutputStream(fName);
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(oFile, "GBK"));
         logger.info("保存配置文件：" + fName);
         props.store(bw, "");
         oFile.close();
      } catch (FileNotFoundException e) {
         logger.error("配置文件" + fName + "写错误。");
      } catch (IOException e) {
         logger.error("配置文件" + fName + "写错误。");
      } finally {
         if (oFile != null) {
            try {
               oFile.close();
               oFile = null;
            } catch (IOException e) {
               logger.error("配置文件操作错误。");
            }
         }
      }
   }

   protected String genGBKString(byte[] bytes) throws UnsupportedEncodingException {
      if (bytes == null) return "";
      else return new String(bytes, "GBK");

   }

   public String getFilePath() {
      File file = new File(Constant.DIR_DAT + this.catalogName);
      if (!file.exists()) file.mkdirs();

      return file.getAbsolutePath() + File.separator;
   }

   public String getFileName() {
      return fileName;
   }

}
