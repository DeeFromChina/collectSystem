package com.golead.disService.service;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DataCleanProcess extends Thread {

   private static Logger logger = Logger.getLogger(DataCleanProcess.class);

   private Properties    properties;
   private long          keepTime;

   public DataCleanProcess(Map<String, Object> initParameters) {
      this.properties = (Properties) initParameters.get("properties");
      Object kd = properties.get("ftpServer.keep.days");
      keepTime = (kd == null || kd.toString().equals("")) ? 30L : new Long(kd.toString());
      keepTime = keepTime * 24L * 60L * 60L * 1000L;
   }

   public void run() {
      logger.info("数据清除开始。");
      try {
         cleanFile();
      } catch (Exception e) {
         e.printStackTrace();
         logger.info("数据清除失败。");
      }
      logger.info("数据清除结束。");
   }

   private Date today = new Date();

   private void cleanFile() throws Exception {
      String path = properties.get("ftpServer.dasDataDir") == null ? "" : properties.get("ftpServer.dasDataDir").toString();
      File comFiles = new File(path);
      if (!comFiles.exists()) {
         logger.info("数据清除失败！");
         return;
      }
      for (File comFile : comFiles.listFiles()) {
         if (comFile.isDirectory()) {
            for (File stationFile : comFile.listFiles()) {
               if (stationFile.isDirectory()) deleteDirectory(stationFile);
               else if ((today.getTime() - stationFile.lastModified()) > keepTime) {
                  logger.debug("删除文件："+stationFile.getAbsolutePath());
                  stationFile.delete();
               }
            }
         }
      }
   }

   public void deleteDirectory(File dirFile) {
      try {
         File[] files = dirFile.listFiles();
         for (File f : files) {
            if (f.isFile()) {
               if ((today.getTime() - f.lastModified()) > keepTime) {
                  logger.debug("删除文件："+f.getAbsolutePath());
                  f.delete();
               }
            }
            else if (f.isDirectory()) deleteDirectory(f);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
