package com.golead.dasService.collection;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

public class CollectionLogImpl implements CollectionLogInterface {

   @Override
   public void collectionLog(String path) throws Exception {
      try {
         SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
         
         path = path + "/InfoGather_";
         File logFile = new File(path);
//         InfoGather_12.28.log
         if(!logFile.exists()){
            
         }
         RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
         randomFile.seek(lastTimeFileSize);
         String tmp = null;
         while ((tmp = randomFile.readLine()) != null) {
//             System.out.println(dateFormat.format(new Date()) + "\t"
//                     + tmp);
         }
         lastTimeFileSize = randomFile.length();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   private File logFile = null;
   private long lastTimeFileSize = 0; // 上次文件大小
   private static SimpleDateFormat dateFormat = new SimpleDateFormat(
           "yyyy-MM-dd HH:mm:ss");

//   public LogReader(File logFile) {
//       this.logFile = logFile;
//       lastTimeFileSize = logFile.length();
//   }

   /**
    * 实时输出日志信息
    */
   public void run() {
       while (true) {
//           try {
//               RandomAccessFile randomFile = new RandomAccessFile(logFile, "r");
//               randomFile.seek(lastTimeFileSize);
//               String tmp = null;
//               while ((tmp = randomFile.readLine()) != null) {
//                   System.out.println(dateFormat.format(new Date()) + "\t"
//                           + tmp);
//               }
//               lastTimeFileSize = randomFile.length();
//           } 
//           catch (IOException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//           }
//           try {
//               Thread.sleep(500);
//           } catch (InterruptedException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//           }
       }
   }
}
