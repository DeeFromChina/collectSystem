package com.golead.disService.comm.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DataTransactionThread extends Thread {
   private static Logger logger = Logger.getLogger(DataTransactionThread.class);
   private List<String>  sqlList;
   private Properties    properties;
   private String        tableName;

   public DataTransactionThread(Properties properties, List<String> sqlList, String tableName) {
      this.properties = properties;
      this.sqlList = sqlList;
      this.tableName = tableName;
   }

   @Override
   public void run() {
      Connection connection = openConnection();
      if (connection == null) return;
      execTransaction(connection);
      closeConnection(connection);
   }

   public boolean execTransaction(Connection connection) {
      boolean isSuccess = false;
      Statement stmt = null;

      int line = 0;
      try {
         connection.setAutoCommit(false);

         stmt = connection.createStatement();
         for (int i = 0; i < sqlList.size(); i++) {
            stmt.addBatch(sqlList.get(i).toString());
            if (i % 1000 == 999) {
               stmt.executeBatch();
               connection.commit();
               line++;
               logger.info(tableName + "数据插入成功，第" + line * 500 + "行。");
               stmt.clearBatch();
            }
         }
         stmt.executeBatch();
         connection.commit();
         line++;
         logger.info(tableName + "数据插入成功，第" + Integer.valueOf(sqlList.size() / 2) + "行。");

         isSuccess = true;
      } catch (Exception e) {
         e.printStackTrace();
         logger.error(tableName + "(" + line * 500 + ")数据插入错误：" + e.getMessage());
         try {
            connection.rollback();
         } catch (SQLException e1) {
            e1.printStackTrace();
         }
         isSuccess = false;
      } finally {
         try {
            connection.setAutoCommit(true);
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      return isSuccess;
   }

   private Connection openConnection() {
      String hostname = properties.getProperty("server.severName");
      String port = properties.getProperty("server.port");
      String userName = properties.getProperty("server.dc.userName");
      String password = properties.getProperty("server.dc.password");
      String url = "jdbc:oracle:thin:@" + hostname + ":" + port + ":orcl";

      try {
         Connection c = DriverManager.getConnection(url, userName, password);
         logger.info("数据库已连接。");
         return c;
      } catch (SQLException e) {
         logger.error("数据库连接失败。");
         return null;
      }
   }

   private void closeConnection(Connection connection) {
      try {
         if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("数据库已关闭。");
         }
      } catch (SQLException e) {
         logger.error("数据库关闭失败。");
         connection = null;
      }
   }
}
