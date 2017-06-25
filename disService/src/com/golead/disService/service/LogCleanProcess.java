package com.golead.disService.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class LogCleanProcess extends Thread {
   
   private static Logger      logger = Logger.getLogger(LogCleanProcess.class);
   
   private String           filePath = System.getProperty("user.dir") + "/log/";

   public LogCleanProcess() {
   }
   
   public void run() {
      logger.info("日志清除开始。");
      try {
         cleanLog();
      }
      catch (Exception e) {
         e.printStackTrace();
         logger.info("日志清除失败。");
      }
      logger.info("日志清除结束。");
   }
   
   private void cleanLog() throws Exception{
      File[] files = new File(filePath).listFiles();
      String nowtime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      for(File file : files){
         long NowTime = sdf.parse(nowtime).getTime();
         long fileTiem = file.lastModified()+30*360000*24;
         if(fileTiem<NowTime){
            file.delete();
         }
      }
   }
}
