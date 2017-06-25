package com.golead.disManager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.golead.disManager.util.ResultSetMap;

public class JdbcDao {

   private static Logger      logger     = Logger.getLogger(JdbcDao.class);

   private static Properties  properties;

   protected SimpleDateFormat sdf        = new SimpleDateFormat("yyyyMMddHHmmss");

   private static Connection  connection = null;

   private JdbcDao() {
   }

   static {
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
      } catch (ClassNotFoundException e) {
         throw new ExceptionInInitializerError(e);
      }
   }

   public static void setProperties(Properties props) {
      properties = props;
   }

   public static List<ResultSetMap> query(String sql) {
      Statement stmt = null;
      ResultSet rs = null;
      try {
         if (connection == null) openConnection();
         if (connection.isClosed()) {
            closeConnection();
            openConnection();
         }
         stmt = connection.createStatement();
         rs = stmt.executeQuery(sql);
         List<ResultSetMap> list = new ArrayList<ResultSetMap>();
         if (rs == null) return list;
         ResultSetMetaData rsmd = rs.getMetaData();
         while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
               String key = rsmd.getColumnName(i);
               key = key.toLowerCase();
               map.put(key, rs.getObject(i));
            }
            list.add(new ResultSetMap(map));
         }
         return list;
      } catch (SQLException e) {
         logger.error("数据查询错误。");
         logger.error(e);
         return null;
      } finally {
         colseObject(stmt, rs);
      }
   }

   public static Integer execute(String sql) {
      Statement stmt = null;
      try {
         if (connection == null) openConnection();
         if (connection.isClosed()) {
            closeConnection();
            openConnection();
         }
         stmt = connection.createStatement();
         return stmt.executeUpdate(sql);
      } catch (SQLException e) {
         logger.error("数据操作失败。");
         logger.error(e);
         return null;
      } finally {
         colseObject(stmt);
      }
   }

   private static void colseObject(Statement stmt) {
      if (stmt != null) {
         try {
            stmt.close();
            stmt = null;
         } catch (SQLException e) {
            logger.error("数据集操作错误。");
         }
      }
   }

   private static void colseObject(Statement stmt, ResultSet rs) {
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
   }

   public static void openConnection() {
      String hostname = properties.getProperty("server.severName");
      String port = properties.getProperty("server.port");
      String databaseName = properties.getProperty("server.dbName");
      String userName = properties.getProperty("server.userName");
      String password = properties.getProperty("server.password");
      String url = "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + databaseName;

      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         connection = DriverManager.getConnection(url, userName, password);
         logger.info("数据库已连接。");
      } catch (SQLException e) {
         logger.error("数据库连接失败。");
         connection = null;
      } catch (ClassNotFoundException e) {
         logger.error("找不到数据库驱动。");
         e.printStackTrace();
      }
   }

   public static void closeConnection() {
      try {
         if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("数据库已关闭。");
         }
      } catch (SQLException e) {
         logger.error("数据库关闭失败。");
      }
   }
}
