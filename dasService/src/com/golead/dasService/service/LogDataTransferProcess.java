package com.golead.dasService.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.golead.dasService.common.utils.Search;

public class LogDataTransferProcess extends Thread {

   private static Logger logger = Logger.getLogger(LogDataTransferProcess.class);

   private Properties    properties;

   private String        stationId;

   private String        stationName;

   private FTPClient     ftpClient;
   
   public LogDataTransferProcess(Map<String, Object> initParameters) {
      this.properties = (Properties) initParameters.get("properties");
      this.stationId = properties.getProperty("company.stationID");
      this.stationName = properties.getProperty("company.stationName");
   }
   
   public void run() {
      logger.info("数据传送开始。");

      if (stationId == null || "".equalsIgnoreCase(stationId) || (stationName == null || "".equalsIgnoreCase(stationName))) {
         logger.error("找不到加油站编号和名称。");
         return;
      }

      openConnection();

      transformData();

      closeConnection();
      
      logger.info("数据传送结束。");
   }

   private void openConnection() {
      String server = properties.getProperty("ftpServer.severIP");
      String port = properties.getProperty("ftpServer.port");
      String userName = properties.getProperty("ftpServer.userName");
      String password = properties.getProperty("ftpServer.password");
      ftpClient = new FTPClient();
      try {
         ftpClient.connect(server, new Integer(port));
         int reply = ftpClient.getReplyCode();
         if (!FTPReply.isPositiveCompletion(reply)) {
            logger.info("ftp服务器连接失败。");
            ftpClient.disconnect();
            ftpClient = null;
         }
         boolean res = ftpClient.login(userName, password);
         for (String str : ftpClient.getReplyStrings())
            logger.info(str);

         if (res) {
            int lPort = ftpClient.getLocalPort();
            logger.info("ftp服务器已连接,本地端口号:" + lPort);
            ftpClient.pasv();
            for (String str : ftpClient.getReplyStrings())
               logger.info(str);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            for (String str : ftpClient.getReplyStrings())
               logger.info(str);
            ftpClient.setBufferSize(2048);
            ftpClient.setDataTimeout(30000);
         }
         else {
            logger.info("ftp服务器连接失败。");
            ftpClient.disconnect();
            ftpClient = null;
         }
      } catch (SocketException e) {
         logger.error("ftp服务器连接失败。");
         ftpClient = null;
      } catch (IOException e) {
         logger.error("ftp服务器连接失败。");
         ftpClient = null;
      }
   }
   
   private void transformData() {
      if (!setupWorkingDirectory(properties.getProperty("company.companyNo").toString() + File.separator + this.stationId)) return;
      String logFilePath = System.getProperty("user.dir") + File.separator + "logdat";
      File logFile = new File(logFilePath);
      if(!logFile.exists()){
         return;
      }
      List<String> zipList = Search.zipFilePath(logFilePath);
      if (zipList == null || zipList.size() == 0) { return; }
      for (String path : zipList) {
         if (path.endsWith(".zip")) {
            File cfg = new File(path.replace(".zip", ".cfg"));
            if (!cfg.exists()) {
               continue;
            }
            File zipFile = new File(path);
            if(!zipFile.exists()){
               continue;
            }
            boolean res = uploadFile(zipFile.getName(), zipFile);
            if (res) {
               boolean iscfg = uploadFile(cfg.getName(), cfg);
               if(iscfg){
                  cfg.delete();
               }
            }
         }
      }
   }

   private boolean uploadFile(String saveName, File file) {
      BufferedInputStream bis = null;
      boolean res = true;
      try {

         bis = new BufferedInputStream(new FileInputStream(file));
         String upFileName = new String(saveName.getBytes("UTF-8"), "iso-8859-1");
         ftpClient.enterLocalPassiveMode();
         if (ftpClient.storeFile(upFileName, bis)) {
            logger.info("文件上传成功：" + file.getName());
         }
         else {
            logger.error("文件上传失败：" + file.getName());
            res = false;
         }

      } catch (IOException e) {
         logger.error("ftp服务器操作失败。");

      } finally {
         try {
            if (bis != null) bis.close();
         } catch (IOException e) {
            logger.error("ftp服务器操作失败。");
         }
      }

      return res;
   }
   
   private void closeConnection() {
      try {
         ftpClient.logout();
         ftpClient.disconnect();
         logger.info("ftp服务器已关闭。");
      } catch (IOException e) {
         logger.error("ftp服务器退出错误。");
      }
   }

   private boolean setupWorkingDirectory(String workDir) {
      if (ftpClient == null) return false;

      workDir = "dasLogData" + File.separator + workDir;
      boolean res = true;
      try {
         String[] folders = workDir.replaceAll("\\\\", "/").split("/");
         for (String folder : folders) {
            res = ftpClient.changeWorkingDirectory(folder);
            if (!res) {
               res = ftpClient.makeDirectory(folder);
               res = ftpClient.changeWorkingDirectory(folder);
               if (!res) {
                  logger.error("ftp服务器操作失败。");
               }
            }
         }

      } catch (IOException e) {
         logger.error("ftp服务器操作失败。");
         res = false;
      }

      return res;
   }
}
