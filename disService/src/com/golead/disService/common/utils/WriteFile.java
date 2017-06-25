package com.golead.disService.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class WriteFile {

   public static void writeLog(List<String> list) throws Exception {
      if(list == null || list.size() == 0){
         return;
      }
      BufferedWriter fw = null;
      try {
         String logFilePath = System.getProperty("user.dir") + File.separator + "log1";
         File file = new File(logFilePath);
         if (!file.exists()) {
            file.mkdirs();
         }
         String logFileSonPath = logFilePath + File.separator + "123.txt";
         File logFile = new File(logFileSonPath);
         if (!logFile.exists()) {
            logFile.createNewFile();
         }
         fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "ISO-8859-1")); // 指定编码格式，以免读取时中文字符异常
         for (String s : list) {
            fw.append(s);
            fw.newLine();
         }
         fw.flush();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      finally {
         if (fw != null) {
            try {
               fw.close();
            }
            catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
