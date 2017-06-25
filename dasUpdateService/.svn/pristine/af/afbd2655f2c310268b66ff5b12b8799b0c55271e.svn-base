package com.golead.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.golead.common.util.FileUtil;

public class UpdateService {

   private static Logger      logger         = Logger.getLogger(UpdateService.class);

   private boolean            status         = true;

   protected SimpleDateFormat sdf            = new SimpleDateFormat("yyyyMMddHHmmss");

   private Properties         properties     = new Properties();

   private long               lastModifyTime = 0;

   private String             localVersion;

   private String             newVersion     = null;

   // private FTPClient ftpClient;

   private String             updateType;

   public static void main(String[] args) {
      logger.info("UpdateService服务启动......");
      new UpdateService().run();
      logger.info("UpdateService服务关闭。");
   }

   private void run() {
      boolean result = getProperties();// 读取系统配置文件。如果失败，不能启动数据采集服务。
      if (!result) {
         logger.info("系统更新服务未启动。");
         return;
      }
      while (true) {
         try {
            boolean res = getProperties();// 读取系统配置文件。如果失败，不能启动数据采集服务。
            if (res) {
               goService();
            }
            else {
               logger.info("系统更新服务未启动。");
            }
            // 每隔一秒钟检测一次。
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   public boolean getProperties() {
      File file = new File("config.properties");
      long time = file.lastModified();
      if (lastModifyTime >= time) return true;
      else lastModifyTime = time;

      try {
         Properties prop = new Properties();
         InputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties");
         BufferedReader bf = new BufferedReader(new InputStreamReader(in));
         prop.load(bf);
         properties.clear();
         properties.putAll(prop);
         String sta = properties.getProperty("running.status");
         if (sta != null && "enable".equalsIgnoreCase(sta)) status = true;
         else status = false;
         return true;
      } catch (IOException e) {
         logger.error("读取配置文件config.properties错误。");
         return false;
      }
   }

   private void goService() {
      if (!status) return;

      String fileName = System.getProperty("user.dir") + File.separator + "update.properties";
      File nowFile = new File(fileName);
      updateType = "D";
      localVersion = "0.0.1";
      if (nowFile.exists()) {
         Properties props = FileUtil.loadProperties(nowFile);
         updateType = (props.getProperty("type") == null || props.getProperty("type").equals("")) ? "D" : props.getProperty("type");
         localVersion = (props.getProperty("version") == null || props.getProperty("version").equals("")) ? "0.0.1" : props.getProperty("version");
      }
      Calendar c = Calendar.getInstance();
      int h = c.get(Calendar.HOUR_OF_DAY);
      int m = c.get(Calendar.MINUTE);
      int s = c.get(Calendar.SECOND);
      if (updateType.equals("D")) {
         if (h % 6 == 0 && m == 0 && s == 0) goUpdate();
      }
      else if (updateType.equals("H")) {
         if (m == 0 && s == 0) goUpdate();
      }
      else if (updateType.equals("M")) {
         if (s == 0) goUpdate();
      }
      else {
         goUpdate();
      }
   }

   private Message cmdExec(Runtime runtime, String cmdStr) {
      Process process = null;
      Message rtn = new Message();
      try {
         process = runtime.exec(cmdStr);
         process.waitFor();
         int rtnCode = process.exitValue();
         rtn.setCode(rtnCode);
         InputStream fis = process.getInputStream();
         InputStreamReader isr = new InputStreamReader(fis, "GBK");
         BufferedReader br = new BufferedReader(isr);
         String line = null;
         StringBuffer sb = new StringBuffer("");
         while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
         }
         rtn.setMessage(sb.toString());
         logger.info(sb.toString());
      } catch (IOException | InterruptedException e) {
         rtn.setCode(9999);
         rtn.setMessage(e.getMessage());
      } finally {
         if (process != null) process.destroy();
         process = null;
      }
      return rtn;
   }

   private void goUpdate() {
      try {
         logger.info("系统更新开始。");
         FTPClient ftpClient = openConnection();
         if (ftpClient == null) return;
         boolean res = checkVersion(ftpClient);
         closeConnection(ftpClient);
         if (!res) return;

         String cmdStr = "";
         Runtime runtime = Runtime.getRuntime();
         Message msg;

         logger.info("运行系统更新程序。");
         String batName = System.getProperty("user.dir") + File.separator + "update.bat ";
         String commandStr = batName + newVersion;

         cmdStr = "cmd /c " + commandStr;
         msg = cmdExec(runtime, cmdStr);
         if (msg.code != 0) {
            logger.info("系统更新失败。");
            return;
         }
         savePropoerties();
         logger.info("系统更新完毕。");
      } catch (Exception e) {
         logger.error("系统错误：" + e.getMessage());
         logger.error(e);
      }
   }

   private void savePropoerties() {
      String fileName = System.getProperty("user.dir") + File.separator + "update.properties";
      File file = new File(fileName);
      Properties props = new Properties();
      props.put("type", updateType);
      props.put("version", newVersion);
      FileUtil.saveProperties(file, props);
   }

   private boolean checkVersion(FTPClient ftpClient) {
      if (!ftpClient.isConnected()) return false;
      logger.info("检查系统版本.....");
      FTPFile[] ftpFiles;
      try {
         ftpClient.cwd("update");
         ftpFiles = ftpClient.listFiles();
      } catch (IOException e) {
         logger.error("找不到系统版本信息。");
         return false;
      }
      if (ftpFiles == null || ftpFiles.length == 0) {
         logger.info("找不到系统版本信息。");
         return false;
      }

      String version = "";
      String reg = "^\\d";
      Pattern pattern = Pattern.compile(reg);
      for (FTPFile f : ftpFiles) {
         if (!f.isDirectory()) continue;
         String fname = f.getName().substring(0, 1);
         Matcher matcher = pattern.matcher(fname);
         if (!matcher.matches()) continue;
         if (f.getName().compareTo(version) > 0) {
            version = f.getName();
         }
      }
      if (localVersion.compareTo(version) >= 0) {
         logger.info("系统已经是最新版本，不需要更新。");
         return false;
      }
      try {
         ftpClient.cwd(version);
      } catch (IOException e) {
         logger.error("获取最近版本目录失败，系统无法更新。");
         return false;
      }
      newVersion = version;

      String wd = System.getProperty("user.dir") + File.separator + "update";
      File file = new File(wd);
      if (!file.exists()) file.mkdir();

      wd += File.separator + newVersion;
      File file1 = new File(wd);
      if (!file1.exists()) file1.mkdir();

      try {
         String[] downFiles = ftpClient.listNames();
         for (String dFileName : downFiles) {
            File sfile = new File(wd + File.separator + dFileName);
            FileOutputStream fos = new FileOutputStream(sfile);
            ftpClient.retrieveFile(dFileName, fos);
            fos.flush();
            fos.close();
         }
      } catch (IOException e) {
         logger.error("文件下载失败，系统无法更新。");
         return false;
      }
      return true;
   }

   private FTPClient openConnection() {
      String server = properties.getProperty("ftpServer.severIP");
      String port = properties.getProperty("ftpServer.port");
      String userName = properties.getProperty("ftpServer.userName");
      String password = properties.getProperty("ftpServer.password");
      FTPClient ftpClient = new FTPClient();
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
      return ftpClient;
   }

   private void closeConnection(FTPClient ftpClient) {
      if (ftpClient == null) return;
      if (!ftpClient.isConnected()) return;
      try {
         ftpClient.logout();
         ftpClient.disconnect();
         logger.info("ftp服务器已关闭。");
      } catch (IOException e) {
         logger.error("ftp服务器退出错误。");
      }
   }

   class Message {
      private int    code;
      private String message;

      public int getCode() {
         return code;
      }

      public void setCode(int code) {
         this.code = code;
      }

      public String getMessage() {
         return message;
      }

      public void setMessage(String message) {
         this.message = message;
      }
   }
}
