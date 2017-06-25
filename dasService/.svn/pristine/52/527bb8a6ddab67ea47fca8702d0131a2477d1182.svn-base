package com.golead.dasService.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionInterface;
import com.golead.dasService.common.Command;
import com.golead.dasService.common.Constant;
import com.golead.dasService.common.ProcessCallback;
import com.golead.dasService.common.utils.DateUtil;
import com.golead.dasService.common.utils.FileUtil;
import com.golead.dasService.common.utils.ZipUtil;

public class DataCollectProcess extends Thread {

   private static Logger   logger = Logger.getLogger(DataCollectProcess.class);

   private Properties      properties;

   private String          type;

   private final Date      startDate;

   private final Date      endDate;

   private Connection      connection;

   private ProcessCallback processCallback;

   public DataCollectProcess(Map<String, Object> initParameters, ProcessCallback processCallback) {
      this.properties = (Properties) initParameters.get("properties");
      this.startDate = (Date) initParameters.get("startDate");
      this.endDate = (Date) initParameters.get("endDate");
      this.type = (String) initParameters.get("type");
      this.processCallback = processCallback;
   }

   public void run() {
      logger.info("数据采集开始。");
      try {
         Class.forName("interbase.interclient.Driver");
      } catch (ClassNotFoundException e) {
         logger.error("找不到数据库驱动程序。");
         return;
      }
      openConnection();
      if (connection != null) {
         generateData();
         closeConnection();
         logger.info("数据采集结束。");
      }
      processCallback.callBack(properties.getProperty("type"));
   }

   private void generateData() {
      String packageName = null;
      try {
         URLClassLoader cl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
         URL urlPath = cl.getResource("com/golead/dasService/dao");
         if (urlPath == null) {
            URL[] urls = cl.getURLs();
            for (URL url : urls) {
               String fName = url.getFile();
               if (fName.indexOf("dasService.jar") > 0) {
                  packageName = fName;
               }
            }
         }
         else {
            String fName = urlPath.getFile();
            if (fName.indexOf("!") > 0) {
               packageName = fName.substring(6, fName.indexOf("!"));
            }
            else packageName = fName;
         }
         if (packageName == null) return;

         String catalogName = properties.getProperty("company.stationID")+DateUtil.getTimeStamp();
         Properties cfg = FileUtil.loadDataCfg(catalogName);
         cfg.setProperty("station.id", properties.getProperty("company.stationID"));
         cfg.setProperty("station.name", properties.getProperty("company.stationName"));
         cfg.setProperty("catalog.name", catalogName);
         cfg.setProperty("catalog.path", Constant.DIR_DAT + catalogName + File.separator);
         cfg.setProperty("catalog.createTime", DateUtil.lFormat(new Date()));
         cfg.setProperty("catalog.status", Constant.STATUS_START);
         cfg.setProperty("catalog.startDate", DateUtil.lFormat(startDate));
         cfg.setProperty("catalog.endDate", DateUtil.lFormat(endDate));
         FileUtil.saveDataCfg(catalogName, cfg);
         logger.info("生成数据配置文件：" + Constant.DIR_DAT + catalogName + Constant.SFX_CFG);

         boolean collectResult = true;
         Properties latestCollectStatus = FileUtil.loadLatestCollectStatus();

         String[] list = {};
         logger.info("packageName:" + packageName);

         int idx = packageName.indexOf("jar");
         if (idx > 0) {
            List<String> lst = new ArrayList<String>();
            File jarFile = new File(packageName);
            JarInputStream jarInput;
            try {
               jarInput = new JarInputStream(new BufferedInputStream(new FileInputStream(jarFile)));
               JarEntry entry = jarInput.getNextJarEntry();
               while (entry != null) {
                  if (entry.getName().startsWith("com/golead/dasService/dao/")) {
                     lst.add(entry.getName().replaceAll("com/golead/dasService/dao/", ""));
                  }
                  entry = jarInput.getNextJarEntry();
               }
            } catch (IOException e) {
               logger.error("找不到数据类。");
            }
            list = new String[lst.size()];
            for (int i = 0; i < lst.size(); i++)
               list[i] = lst.get(i);
         }
         else {
            File file = new File(packageName);
            list = file.list();
         }

         for (String f : list) {
            if (!f.startsWith("Collect")) continue;
            String className = f.replaceAll(".class", "");
            Class<?> cc = Class.forName("com.golead.dasService.dao." + className);
            Object object = cc.newInstance();
            CollectionInterface tc = (CollectionInterface) object;
            tc.init(catalogName, connection, properties);

            Map<String, Object> paras = new HashMap<String, Object>();
            paras.put("startDate", startDate);
            paras.put("endDate", endDate);
            boolean res = tc.goCollection(paras);
            
            if (res) {
               latestCollectStatus.put("data." + tc.getFileName() + ".lastDate", DateUtil.lFormat(endDate));
               latestCollectStatus.put("data." + tc.getFileName() + ".lastStatus", "finish");
            }
            else {
               latestCollectStatus.put("data." + tc.getFileName() + ".lastErrorDate", DateUtil.lFormat(endDate));
               latestCollectStatus.put("data." + tc.getFileName() + ".lastStatus", "collectError");
            }
          
            if (type.equals(Command.COMMAND_AUTOCOLLECT))  FileUtil.saveLatestCollectStatus(latestCollectStatus);

            if (collectResult) collectResult = res;
         }
          
         if (collectResult) {
            latestCollectStatus.put("collect.lastDate", DateUtil.lFormat(endDate));
            latestCollectStatus.put("collect.lastStatus", "finish");
         }
         else {
            latestCollectStatus.put("collect.lastErrorDate", DateUtil.lFormat(endDate));
            latestCollectStatus.put("collect.lastStatus", "collectError");
         }
         
         if (type.equals(Command.COMMAND_AUTOCOLLECT)) FileUtil.saveLatestCollectStatus(latestCollectStatus);

         cfg = FileUtil.loadDataCfg(catalogName);
         cfg.setProperty("catalog.collectedTime", DateUtil.lFormat(new Date()));
         cfg.setProperty("catalog.status", Constant.STATUS_COLLECTED);

         String targetPath = ZipUtil.zip(cfg.getProperty("catalog.path"), ZipUtil.PASSWD);
         if (targetPath != null) {
            cfg.setProperty("catalog.zipTime", DateUtil.lFormat(new Date()));
            cfg.setProperty("catalog.status", Constant.STATUS_ZIP);
            cfg.setProperty("catalog.md5", FileUtil.md5File(targetPath));
         }

         FileUtil.saveDataCfg(catalogName, cfg);
         logger.info("更新数据配置文件：" + Constant.DIR_DAT + catalogName + Constant.SFX_CFG);
      } catch (ClassNotFoundException e) {
         logger.error("找不到数据类(classnotfound)。"+e.getMessage());
      } catch (InstantiationException e) {
         logger.error("找不到数据类(instantiation)。"+e.getMessage());
      } catch (IllegalAccessException e) {
         logger.error("找不到数据类(IllegalAccess)。"+e.getMessage());
      }
   }

   private void openConnection() {
      String hostname = properties.getProperty("server.severName");
      String port = properties.getProperty("server.port");
      String databaseName = properties.getProperty("server.dbName");
      String userName = properties.getProperty("server.userName");
      String password = properties.getProperty("server.password");
      String url = "jdbc:interbase://" + hostname + ":" + port + "/" + databaseName;

      try {
         connection = DriverManager.getConnection(url, userName, password);
         logger.info("数据库已连接。");
      } catch (SQLException e) {
         logger.error("数据库连接失败。");
         connection = null;
      }
   }

   private void closeConnection() {
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
