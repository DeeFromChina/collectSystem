package com.golead.disService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.golead.disService.callback.DataLoadProcessCallback;
import com.golead.disService.common.utils.ZipUtil;
import com.golead.disService.common.utils.Search;
import com.golead.disService.dao.DataSource;
import com.golead.disService.rmi.RmiServer;
import com.golead.disService.service.DataCleanProcess;
import com.golead.disService.service.DataLoadProcess;
import com.golead.disService.service.DataLogProcess;

public class DisService {

   private static Logger      logger         = Logger.getLogger(DisService.class);

   protected SimpleDateFormat sdf            = new SimpleDateFormat("yyyyMMddHHmmss");

   private Thread             dataLoadProcess;

   private Thread             rmiServer;

   private Thread             dataCleanProcess;
   
   private Thread             dataLogProcess;

   private Properties         properties     = new Properties();

   private long               lastModifyTime = 0;

   private int                intervalTime   = 1;

   /**
    * @param args
    */
   public static void main(String[] args) {
      // PropertyConfigurator.configure(System.getProperty("user.dir") +
      // "/disService-log4j.properties");
      DisService disService = new DisService();
      disService.run();
   }

   private void run() {
      boolean result = getProperties();// 读取系统配置文件。如果失败，不能启动数据加载服务。
      if (!result) {
         logger.info("数据加载服务未启动。");
         return;
      }
      setDataSource();
      startRmiServer();
      while (true) {
         try {
            goLogService();
            boolean res = getProperties();
            if (res) {
               goService();
            }
            else {
               logger.info("数据采集服务未启动。");
            }
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }
   
   private void goLogService() throws InterruptedException {
      startDataLog();
   }

   private void startRmiServer() {
      if (rmiServer != null && rmiServer.isAlive()) {
         logger.debug("日志数据采集进程进行中，不能启动.....");
         return;
      }
      rmiServer = new RmiServer();
      rmiServer.start();
   }

   private void setDataSource() {
      String hostname = properties.getProperty("server.severName");
      String port = properties.getProperty("server.port");
      String userName = properties.getProperty("server.dc.userName");
      String password = properties.getProperty("server.dc.password");
      String url = "jdbc:oracle:thin:@" + hostname + ":" + port + ":orcl";

      Properties p = new Properties();
      p.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
      p.setProperty("url", url);
      p.setProperty("password", password);
      p.setProperty("username", userName);
      p.setProperty("maxActive", "30");
      p.setProperty("maxIdle", "10");
      p.setProperty("maxWait", "1000");
      p.setProperty("removeAbandoned", "false");
      p.setProperty("removeAbandonedTimeout", "120");
      p.setProperty("testOnBorrow", "true");
      p.setProperty("logAbandoned", "true");
      DataSource.init(p);
   }

   private void goService() throws InterruptedException {
      startLoadData();
      Calendar d = Calendar.getInstance();
      int h = d.get(Calendar.HOUR);
      int m = d.get(Calendar.MINUTE);
      int s = d.get(Calendar.SECOND);
      if (s == 0 && m == 0 && h % 6 == 0) {
         startDataClean();
      }
   }

   private int startLoadData() throws InterruptedException {
      // 避免数据采集进程重复启动。
      if (dataLoadProcess != null && dataLoadProcess.isAlive()) {
         logger.debug("数据采集进程进行中，不能启动.....");
         return 1;
      }

      Map<String, Object> initParameters = new HashMap<String, Object>();
      initParameters.put("properties", properties);
      dataLoadProcess = new DataLoadProcess(initParameters, new DataLoadProcessCallback() {
         @Override
         public void processCallback(int code, Map<String, Object> parameters) {
            if (code == DataLoadProcessCallback.CODE_CONNECT_FAILURE) {
               intervalTime = 1;
            }
            else if (code == DataLoadProcessCallback.CODE_CONNECT_SUCESS) {
               intervalTime = 0;
            }
         }
      });
      dataLoadProcess.start();
      Thread.sleep(2000);
      return 0;
   }

   private int startDataClean() throws InterruptedException {
      // 避免数据清除进程重复启动。
      if (dataCleanProcess != null && dataCleanProcess.isAlive()) {
         logger.debug("数据清除进程进行中，不能启动.....");
         return 1;
      }
      Map<String, Object> initParameters = new HashMap<String, Object>();
      initParameters.put("properties", properties);
      dataCleanProcess = new DataCleanProcess(initParameters);
      dataCleanProcess.start();
      Thread.sleep(2000);
      return 0;
   }

   public boolean getProperties() {
      File file = new File("config.properties");
      long time = file.lastModified();
      if (lastModifyTime >= time) return true;
      else lastModifyTime = time;

      try {
         Properties prop = new Properties();
         InputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties");
         prop.load(in);
         properties.clear();
         properties.putAll(prop);
         return true;
      } catch (IOException e) {
         logger.error("读取配置文件config.properties错误。");
         return false;
      }
   }
   
   private int startDataLog() throws InterruptedException {
      // 避免日志数据进程重复启动。
      if (dataLogProcess != null && dataLogProcess.isAlive()) {
         logger.debug("日志数据进程进行中，不能启动.....");
         return 1;
      }
      Map<String, Object> initParameters = new HashMap<String, Object>();
      initParameters.put("properties", properties);
      dataLogProcess = new DataLogProcess(initParameters);
      dataLogProcess.start();
      Thread.sleep(2000);
      return 0;
   }
   
}
