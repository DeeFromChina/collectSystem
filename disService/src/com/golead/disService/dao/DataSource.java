package com.golead.disService.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.log4j.Logger;

public class DataSource {
   private static Logger           logger = Logger.getLogger(DataSource.class);
   private static BasicDataSource dataSource = null;

   public DataSource() {

   }

   public static void init(Properties p) {
      if (dataSource != null) {
         try {
            dataSource.close();
         } catch (Exception e) {
            logger.info("数据库连接池关闭失败。");
         }
         dataSource = null;
      }

      try {
         dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(p);
      } catch (Exception e) {
         logger.info("数据库连接池建立失败。");
      }
   }

   public static synchronized Connection getConnection() throws SQLException {
      Connection conn = null;
      if (dataSource != null) {
         conn = dataSource.getConnection();
      }
      return conn;
   }
}
