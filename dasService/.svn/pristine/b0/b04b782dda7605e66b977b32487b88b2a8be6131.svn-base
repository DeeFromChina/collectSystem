package com.golead.dasService.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;

import com.golead.dasService.common.Constant;
import com.golead.dasService.common.utils.FileUtil;
import com.golead.dasService.filter.NameFilter;

public class DataCleanProcess extends Thread {

   private static Logger logger = Logger.getLogger(DataCleanProcess.class);

   private Properties    properties;

   private final String  saveDay;

   private final File[]  fileList;

   public DataCleanProcess(Map<String, Object> initParameters) {
      this.properties = (Properties) initParameters.get("properties");
      this.saveDay = initParameters.get("saveDay").toString();
      Object objFL = initParameters.get("fileList");
      if (objFL != null) {
         this.fileList = (File[]) objFL;
      }
      else {
         this.fileList = null;
      }
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

   private void cleanFile() throws Exception {
      Integer sd = Integer.valueOf(saveDay);
      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, -1 * sd);
      String dateString = String.valueOf(c.get(Calendar.YEAR) * 10000 + (c.get(Calendar.MONTH) + 1) * 100 + c.get(Calendar.DAY_OF_MONTH)) + "000000";

      for (File file : fileList) {
         if (dateString.compareTo(file.getName()) > 0) continue;
         Properties prop = FileUtil.loadProperties(file);
         String status = prop.get("catalog.status") == null ? "" : prop.get("catalog.status").toString();
         if (!"send".equals(status)) continue;

         String fname = file.getPath().replaceAll(".cfg", "");
         boolean res = FileUtil.deleteFile(fname);
         if (res) logger.info("文件已删除：" + fname);
         else logger.error("删除文件失败：" + fname);

         fname = file.getPath().replaceAll("cfg", "zip");
         res = FileUtil.deleteFile(fname);
         if (res) logger.info("文件已删除：" + fname);
         else logger.error("删除文件失败：" + fname);

         fname = file.getPath();
         res = FileUtil.deleteFile(fname);
         if (res) logger.info("文件已删除：" + fname);
         else logger.error("删除文件失败：" + fname);
      }
   }
}
