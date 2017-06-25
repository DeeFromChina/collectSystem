package com.golead.dasService.common.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.golead.dasService.common.Constant;

public class FileUtil {

   private static Logger logger = Logger.getLogger(FileUtil.class);

   public static Properties loadLatestCollectStatus() {
      return loadProperties(new File(Constant.DIR_DAT + "LatestCollectStatus.properties"));
   }

   public static boolean saveLatestCollectStatus(Properties properties) {
      return saveProperties(new File(Constant.DIR_DAT + "LatestCollectStatus.properties"), properties, "");
   }

   public static Properties loadDataCfg(String dataCatalogName) {
      return loadProperties(new File(Constant.DIR_DAT + dataCatalogName + Constant.SFX_CFG));
   }

   public static boolean saveDataCfg(String dataCatalogName, Properties properties) {
      return saveProperties(new File(Constant.DIR_DAT + dataCatalogName + Constant.SFX_CFG), properties);
   }

   public static boolean saveDataCfg(String dataCatalogName, Properties properties, String comments) {
      return saveProperties(new File(Constant.DIR_DAT + dataCatalogName + Constant.SFX_CFG), properties, comments);
   }

   public static Properties loadProperties(File file) {
      boolean hasCfg = true;
      Properties prop = new Properties();
      FileInputStream fis = null;

      try {
         if (!file.exists()) hasCfg = file.createNewFile();
         if (hasCfg) {
            fis = new FileInputStream(file);
            BufferedReader bf = new BufferedReader(new InputStreamReader(fis));
            prop.load(bf);
         }
      } catch (FileNotFoundException e) {
         logger.error("配置文件" + file.getAbsolutePath() + "读错误。");
      } catch (IOException e) {
         logger.error("配置文件" + file.getAbsolutePath() + "读错误。");
      } finally {
         try {
            if (fis != null) fis.close();
         } catch (IOException e) {
            logger.error("配置文件操作错误。");
         }
      }
      return prop;
   }

   public static boolean saveProperties(File file, Properties properties) {
      return saveProperties(file, properties, null);
   }

   public static boolean saveProperties(File file, Properties properties, String comments) {
      if (properties == null) return false;
      try {
         if (!file.exists()) file.createNewFile();

         Enumeration<Object> keys = properties.keys();
         List<String> list = new ArrayList<String>();
         while (keys.hasMoreElements()) {
            list.add(keys.nextElement().toString());
         }
         Collections.sort(list, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
               return arg0.compareTo(arg1);
            }
         });

         FileOutputStream out = new FileOutputStream(file);
         OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");

         if (comments != null) {
            osw.write("#" + comments + "\r\n");
            osw.write("#" + (new Date()).toString() + "\r\n");
         }
         for (String key : list) {
            Object object = properties.get(key);
            osw.write(key + "=");
            String val = object == null ? "" : String.valueOf(object);
            val = val.replaceAll("\\\\", "\\\\\\\\");
            osw.write(val + "\r\n");
         }
         osw.flush();
         osw.close();
         out.close();
         return true;
      } catch (FileNotFoundException e) {
         return false;
      } catch (IOException e) {
         return false;
      }
   }

   public static boolean deleteFile(String fileName) {
      File file = new File(fileName);
      return deleteFile(file);
   }

   public static boolean deleteFile(File file) {
      if (!file.exists()) return true;
      if (file.isDirectory()) {
         for (File f : file.listFiles()) {
            boolean res = deleteFile(f);
            if (!res) return false;
         }
         return file.delete();
      }
      else return file.delete();
   }

   public static String md5File(String path) {
      return md5File(new File(path));
   }

   public static String md5File(File file) {
      String res = "";
      try {
         res = DigestUtils.md5Hex(new FileInputStream(file));
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return res;
   }

}
