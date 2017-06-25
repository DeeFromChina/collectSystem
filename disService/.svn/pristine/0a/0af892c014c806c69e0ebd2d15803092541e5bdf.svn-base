package com.golead.disService.service;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.golead.common.util.FileUtil;
import com.golead.common.util.NameFilter;
import com.golead.disService.callback.DataLoadProcessCallback;
import com.golead.disService.callback.ExecuteFileCallback;
import com.golead.disService.common.Constant;
import com.golead.disService.common.utils.DateUtil;
import com.golead.disService.common.utils.ZipUtil;
import com.golead.disService.dao.DataSource;
import com.golead.disService.dao.ExecuteFile;

public class DataLoadProcess extends Thread {
   private static Logger           logger = Logger.getLogger(DataLoadProcess.class);

   private Connection              connection;
   private Properties              properties;
   private final String            dasDataDirPath;
   private String                  transType;
   private DataLoadProcessCallback processCallback;

   public DataLoadProcess(Map<String, Object> initParameters, DataLoadProcessCallback processCallback) {
      this.properties = (Properties) initParameters.get("properties");
      this.dasDataDirPath = String.valueOf(this.properties.get("ftpServer.dasDataDir"));
      this.transType = String.valueOf(initParameters.get("transType"));
      this.processCallback = processCallback;
   }

   @Override
   public void run() {
      logger.debug("数据加载开始。");

      List<File> targetFileList = null;
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File nowFile = new File(fileName);
      if (nowFile.exists()) {
         Properties props = FileUtil.loadProperties(nowFile);
         nowFile.delete();
         String methord = (String) props.get("operation");
         if (methord.equals("loadData")) {
            targetFileList = new ArrayList<File>();
            String fStr = (String) props.get("file");
            if (fStr != null && !"".equals(fStr)) {
               String[] fileNames = fStr.split(";");
               for (String fn : fileNames) {
                  targetFileList.add(new File(fn));
               }
            }
         }
      }

      if (targetFileList == null) {
         targetFileList = scanTargetFiles();
      }
      else {
         if (targetFileList.size() > 0) logger.info("完成文件扫描，" + targetFileList.size() + "个文件需要被加载。");
         else logger.debug("完成文件扫描，" + targetFileList.size() + "个文件需要被加载。");
      }

      if (targetFileList.size() > 0) {
         connection = openConnection();
         if (connection != null) {
            processCallback.processCallback(DataLoadProcessCallback.CODE_CONNECT_SUCESS, null);
            unzipData(targetFileList);

            loadData(targetFileList);

            closeConnection(connection);
            unzipClean(targetFileList);
         }
         else {
            processCallback.processCallback(DataLoadProcessCallback.CODE_CONNECT_FAILURE, null);
            logger.info("数据加载过程终止。");
         }
      }
      else {
         logger.debug("没有需要加载的数据文件。");
      }
      logger.debug("数据加载结束。");
   }

   private Connection openConnection() {
      try {
         Connection conn = DataSource.getConnection();
         logger.info("数据库已连接。");
         return conn;
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
      }
   }

   private List<File> scanTargetFiles() {
      logger.debug("开始扫描文件，ftp上传文件夹路径：" + dasDataDirPath);

      final List<File> targetFileList = new ArrayList<File>();
      FileUtil.scanDirectory(dasDataDirPath, new FileUtil.scanDirectoryHandller() {
         @Override
         public int getScanDepth() {
            return 3;
         }

         @Override
         public File[] listFile(int depth, File file) {
            return (depth == getScanDepth() ? file.listFiles(new NameFilter("cfg")) : file.listFiles());
         }

         @Override
         public void handle(int depth, File file) {
            if (depth == getScanDepth()) {
               Properties cfg = FileUtil.loadProperties(file);
               String status = cfg.getProperty("catalog.status");
               if (Constant.STATUS_SENT.equalsIgnoreCase(status) || Constant.STATUS_UNZIP.equalsIgnoreCase(status)) targetFileList.add(file);
            }
         }

      });
      if (targetFileList.size() > 0) logger.info("完成文件扫描，" + targetFileList.size() + "个文件需要被加载。");
      else logger.debug("完成文件扫描，" + targetFileList.size() + "个文件需要被加载。");
      return targetFileList;
   }

   private void unzipClean(List<File> targetFileList) {
      logger.info("开始清理解压临时文件目录。");

      for (File file : targetFileList) {
         unzipClean(file);
      }

      logger.info("临时文件目录清理完毕。");
   }

   private void unzipClean(File cfgFile) {
      Properties cfg = FileUtil.loadProperties(cfgFile);
      String stationId = cfg.getProperty("station.id");

      File zipFile = new File(cfgFile.getAbsolutePath().replaceAll(".cfg", ".zip"));
      try {
         String unzipPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
         String parentFolder = zipFile.getAbsolutePath().replace(this.dasDataDirPath, "");
         parentFolder = parentFolder.substring(0, parentFolder.lastIndexOf(File.separator) + 1);
         unzipPath += parentFolder;
         File unzipDir = new File(unzipPath);
         if (unzipDir.exists() && unzipDir.isDirectory()) {
            File[] files = unzipDir.listFiles();
            for (File f : files) {
               if (f.isDirectory()) {
                  File[] fs = f.listFiles();
                  for (File subf : fs) {
                     subf.delete();
                  }
               }
               f.delete();
            }
         }
         // unzipDir.delete();
         logger.info(String.format("加油站【%s】文件【%s】解压文件清理完毕：%s", stationId, zipFile.getName(), unzipPath));
      } catch (Exception e) {
         e.printStackTrace();
         logger.error(String.format("加油站【%s】文件【%s】解压文件清理失败。", stationId, zipFile.getName()));
         return;
      }
   }

   private void unzipData(List<File> targetFileList) {
      logger.info("开始解压zip文件。");

      for (File file : targetFileList) {
         boolean res = unzipData(file);
      }

      logger.info("完成zip文件解压。");
   }

   private boolean unzipData(File cfgFile) {
      Properties cfg = FileUtil.loadProperties(cfgFile);

      String stationId = cfg.getProperty("station.id");
      String catalogName = cfg.getProperty("catalog.name");

      File zipFile = new File(cfgFile.getAbsolutePath().replaceAll(".cfg", ".zip"));
      if (!zipFile.exists()) {
         logger.error(String.format("加油站【%s】文件【%s】不存在。", stationId, zipFile.getName()));
         cfg.setProperty("catalog.status", Constant.STATUS_FAIL);
         cfg.setProperty("catalog.fail", "压缩文件不存在。");
         FileUtil.saveProperties(cfgFile, cfg);
         return false;
      }

      if (!FileUtil.md5File(zipFile).equals(cfg.get("catalog.md5"))) {
         logger.error(String.format("加油站【%s】文件【%s】不完整。", stationId, zipFile.getName()));
         cfg.setProperty("catalog.status", Constant.STATUS_FAIL);
         cfg.setProperty("catalog.fail", "压缩文件不完整。");
         FileUtil.saveProperties(cfgFile, cfg);
         return false;
      }

      try {
         String unzipPath = System.getProperty("user.dir") + File.separator + "temp" + File.separator;
         String parentFolder = zipFile.getAbsolutePath().replace(this.dasDataDirPath, "");
         parentFolder = parentFolder.substring(0, parentFolder.lastIndexOf(File.separator) + 1);
         unzipPath += parentFolder;
         File unzipDir = new File(unzipPath);
         if (unzipDir.exists() && unzipDir.isDirectory()) {
            File[] files = unzipDir.listFiles();
            for (File f : files)
               f.delete();
         }
         logger.info(String.format("加油站【%s】文件【%s】解压到：%s", stationId, zipFile.getName(), unzipPath));
         ZipUtil.unzip(zipFile.getAbsolutePath(), unzipPath, ZipUtil.PASSWD);

         cfg.setProperty("catalog.status", Constant.STATUS_UNZIP);
         cfg.setProperty("catalog.unzip.path", unzipPath + zipFile.getName().replace(".zip", "") + File.separator);
         cfg.setProperty("catalog.unzip.time", DateUtil.lFormat(new Date()));
         FileUtil.saveProperties(cfgFile, cfg);
         return true;
      } catch (Exception e) {
         e.printStackTrace();
         logger.error(String.format("加油站【%s】文件【%s】解压失败。", stationId, zipFile.getName()));
         cfg.setProperty("catalog.status", Constant.STATUS_FAIL);
         cfg.setProperty("catalog.fail", "文件解压失败。");
         FileUtil.saveProperties(cfgFile, cfg);
         return false;
      }
   }

   private void loadData(List<File> targetFileList) {
      logger.info("开始插入数据。");

      for (File file : targetFileList) {
         executeInsertSql(file);
      }

      logger.info("完成数据插入。");
   }

   private ResultSet checkTable(String table, String columns) {
      String sql = "select " + columns + " from bos_" + table + " where 1=0";
      logger.debug(sql);
      try {
         Statement ps = connection.createStatement();
         return ps.executeQuery(sql);
      } catch (SQLException e) {
         logger.error(e);
         return null;
      }
   }

   private void executeInsertSql(File cfgFile) {
      Properties cfg = FileUtil.loadProperties(cfgFile);
      if (!Constant.STATUS_UNZIP.equalsIgnoreCase(cfg.getProperty("catalog.status"))) return;

      String tableStr = cfg.getProperty("catalog.tables");
      String unzipPath = cfg.getProperty("catalog.unzip.path");
      String stationId = cfg.getProperty("station.id");
      String catalogName = cfg.getProperty("catalog.name");
      String catalogStartDate = cfg.getProperty("catalog.startDate");
      String catalogEndDate = cfg.getProperty("catalog.endDate");
      logger.info(String.format("加油站【%s】上传数据【%s】取数区间【%s至%s】开始插入数据库", stationId, catalogName, catalogStartDate, catalogEndDate));

      final Map<String, Integer> exceuteResult = new HashMap<String, Integer>();
      List<ExecuteFile> exceutes = new ArrayList<ExecuteFile>();

      String[] tables = tableStr.split(";");
      for (String table : tables) {
         String columns = cfg.getProperty("data." + table + ".columns");
         ResultSet rs = checkTable(table, columns); // 检查要导入的数据表是否存在。
         if (rs == null) {
            logger.error(String.format("找不到数据表：%s", table));
            continue;
         }

         ResultSetMetaData rsmd;
         Integer[] types = null;
         try {
            rsmd = rs.getMetaData(); // 获取数据表的数据结构。
            types = new Integer[rsmd.getColumnCount()];
            for (int k = 0; k < rsmd.getColumnCount(); k++) {
               types[k] = rsmd.getColumnType(k + 1);
            }
         } catch (SQLException e1) {
            logger.error(String.format("数据表【%s】错误。", table));
            continue;
         }

         String fileName = cfg.getProperty("data." + table + ".fileName");
         String primaryKey = cfg.getProperty("data." + table + ".primaryKey");

         File dataFile = new File(unzipPath + fileName); // 获取要导入数据文件。
         if (!dataFile.exists()) {
            logger.error(String.format("加油站【%s】文件【%s】缺少数据文件【%s】。", stationId, catalogName, dataFile.getName()));
            continue;
         }

         Map<String, Object> parameters = new HashMap<String, Object>();
         parameters.put("stationId", stationId);
         parameters.put("catalogName", catalogName);
         parameters.put("primaryKey", primaryKey);
         parameters.put("columns", columns);
         parameters.put("types", types);

         ExecuteFile executeFile = new ExecuteFile(dataFile, table, parameters);
         final String t = table;
         executeFile.setExecuteFileCallback(new ExecuteFileCallback() {
            @Override
            public void processCallback(int code, Map<String, Object> parameters) {
               exceuteResult.put(t, code);
            }
         });
         exceutes.add(executeFile);
         executeFile.start();
      }

      boolean wait = true;
      while (wait) {
         try {
            int isBusy = 0;
            for (ExecuteFile es : exceutes) {
               if (es.isAlive()) {
                  isBusy++;
                  // logger.info(es.getTableName());
               }
            }
            if (isBusy == 0) wait = false;
            Thread.sleep(200);
         } catch (InterruptedException e) {
            logger.error("线程错误。");
         }
      }

      boolean isSuccess = exceuteResult.containsValue(ExecuteFileCallback.CODE_PROCESS_FAILUE) ? false : true;
      if (isSuccess) {
         cfg.setProperty("catalog.status", Constant.STATUS_FINISH);
         FileUtil.saveProperties(cfgFile, cfg);
      }
      else {
         cfg.setProperty("catalog.status", Constant.STATUS_FAIL);
         FileUtil.saveProperties(cfgFile, cfg);
      }
   }
}
