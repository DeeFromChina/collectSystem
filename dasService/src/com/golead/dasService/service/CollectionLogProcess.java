package com.golead.dasService.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;










import org.apache.log4j.Logger;

import com.golead.dasService.common.Constant;
import com.golead.dasService.common.utils.DataName;
import com.golead.dasService.common.utils.DateUtil;
import com.golead.dasService.common.utils.FileUtil;
import com.golead.dasService.common.utils.JsonUtils;
import com.golead.dasService.common.utils.ZipUtil;
import com.golead.rmi.DitTransfer;
import com.golead.rmi.OilSaleTransfer;

public class CollectionLogProcess extends Thread {

   private static Logger    logger = Logger.getLogger(CollectionLogProcess.class);

   private Properties       logproperties;
   private Properties       properties;

   private String           logPath;

   private long             rlogsize;

   private long             wlogsize;
      
   private String           zipfileName;

   private SimpleDateFormat sdt    = new SimpleDateFormat("yyyyMMdd");
   
   private String logFilePath ;

   public LogSize           logSize;

   public LogSize getLogSize() {
      return logSize;
   }

   public void setLogSize(LogSize logSize) {
      this.logSize = logSize;
   }

   public CollectionLogProcess(Map<String, Object> initParameters) {
      this.logproperties = (Properties) initParameters.get("logproperties");
      this.properties = (Properties) initParameters.get("properties");
      this.logPath = initParameters.get("logPath").toString();
      this.rlogsize = Long.valueOf(initParameters.get("rlogsize").toString());
      this.wlogsize = Long.valueOf(initParameters.get("wlogsize").toString());
      this.zipfileName = logproperties.get("fileName").toString();
   }

   public void run() {
      logger.info("日志收集开始。");
      try {
    	  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	  collectionLog(list);
      }
      catch (Exception e) {
         e.printStackTrace();
         logger.info("日志收集失败。");
      }
      logger.info("日志收集结束。");
   }
   
   private void collectionLog(List<Map<String, Object>> list) throws Exception {
      try {
         File file = new File(logPath);
         if (!file.exists()) { return; }
         File[] files = file.listFiles();
         File readFile = null;
         String fileName = logproperties.get("fileName").toString().substring(0, 8);
         String collectDay = fileName;
         String today = sdt.format(new Date());
         if(!collectDay.equals(today)){
            today = collectDay;
         }
         for (int i = 0; i < files.length; i++) {
            if (files[i].getName().indexOf("InfoGather") > -1) {
               String fileDay = sdt.format(files[i].lastModified());
               if (today.equals(fileDay)) {
                  readFile = files[i];
                  break;
               }
            }
         }
         if (readFile == null) { return; }
         RandomAccessFile randomFile = new RandomAccessFile(readFile, "r");
//         rlogsize = 0;
         if(rlogsize > randomFile.length()){
            rlogsize = 0;
         }
         randomFile.seek(rlogsize);
         String tmp = null;
         while ((tmp = randomFile.readLine()) != null) {
            analysisLog(tmp, list);
         }
         rlogsize = randomFile.length();
         logger.info(readFile.getName()+String.valueOf(rlogsize)+"行");
         if(list != null && list.size() > 0){
            //writeLog(list);
            transferLog(list);
         }
         logSize.getsize(rlogsize, wlogsize);
//         Thread.sleep(5000);
//         zipLog(zipfileName);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void analysisLog(String lineData, List<Map<String, Object>> list) throws Exception {
      try {
         String stationID = properties.getProperty("company.stationID");
         String companyNo = properties.getProperty("company.companyNo");
         Map<String, Object> lineMap = new HashMap<String, Object>();
         if(lineData.indexOf("{\"data\":") < 0){
            return;
         }
         String data = lineData.substring(lineData.lastIndexOf("{\"data\":"), lineData.length());
         Map<String, Object> map = JsonUtils.Json2Map(data);
         if (map.get("ext_data") != null){ return; }
         if (map.get("data") == null) { return; }
         lineMap.putAll(map);
         lineMap.remove("data");
         Object[] datas = (Object[]) map.get("data");
         for (int i = 0; i < datas.length; i++) {
            Object obj = datas[0];
            Map<String, Object> dataMap = (Map<String, Object>) obj;
            if (dataMap.get("9") == null) { return; }
            if (dataMap.get("11") != null) {
               return;
            }
            else if (dataMap.get("10") != null && dataMap.get("11") == null) {
               lineMap.put(new String(DataName.DataTwo1.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("1"));
               lineMap.put(new String(DataName.DataTwo2.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("2"));
               lineMap.put(new String(DataName.DataTwo3.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("3"));
               lineMap.put(new String(DataName.DataTwo4.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("4"));
               lineMap.put(new String(DataName.DataTwo5.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("5"));
               lineMap.put(new String(DataName.DataTwo6.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("6"));
               lineMap.put(new String(DataName.DataTwo7.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("7"));
               lineMap.put(new String(DataName.DataTwo8.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("8"));
               lineMap.put(new String(DataName.DataTwo9.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("9"));
               lineMap.put(new String(DataName.DataTwo10.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("10"));
               lineMap.put(new String("STATION".getBytes("UTF-8"),"ISO-8859-1"), stationID);
               lineMap.put(new String("COMPANY".getBytes("UTF-8"),"ISO-8859-1"), companyNo);
            }
            else if(dataMap.get("9") != null && dataMap.get("10") == null && dataMap.get("1").toString().length() < 5){
               lineMap.put(new String(DataName.DataFirst1.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("1"));
               lineMap.put(new String(DataName.DataFirst2.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("2"));
               lineMap.put(new String(DataName.DataFirst3.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("3"));
               lineMap.put(new String(DataName.DataFirst4.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("4"));
               lineMap.put(new String(DataName.DataFirst5.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("5"));
               lineMap.put(new String(DataName.DataFirst6.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("6"));
               lineMap.put(new String(DataName.DataFirst7.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("7"));
               lineMap.put(new String(DataName.DataFirst8.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("8"));
               lineMap.put(new String(DataName.DataFirst9.getBytes("UTF-8"),"ISO-8859-1"), dataMap.get("9"));
               lineMap.put(new String("STATION".getBytes("UTF-8"),"ISO-8859-1"), stationID);
               lineMap.put(new String("COMPANY".getBytes("UTF-8"),"ISO-8859-1"), companyNo);
               
            }else{
               return;
            }
         }
         list.add(lineMap);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   private void transferLog(List<Map<String, Object>> list) throws Exception {
      String severIP = properties.get("ftpServer.severIP").toString();
//      String severIP = "192.168.0.110";//"localhost";
      logger.info("rmi开始上传"+severIP);
      String oilTransferPath = "rmi://"+severIP+":9037/oilSaleTransfer";
      String ditTransferPath = "rmi://"+severIP+":9037/ditTransfer";
      OilSaleTransfer ost = (OilSaleTransfer) Naming.lookup(oilTransferPath);
      DitTransfer dit = (DitTransfer) Naming.lookup(ditTransferPath);
      logger.info("rmi已链接1");
      StringBuffer fwDataLog = new StringBuffer();
      for (Map<String, Object> map : list) {
         StringBuffer fwData = new StringBuffer();
         fwData.append("{");
         for(Map.Entry<String, Object> entry : map.entrySet()){
            if(fwData.length() > 10){
               fwData.append(",");
            }
            fwData.append("\"");
            fwData.append(entry.getKey());
            fwData.append("\"");
            fwData.append(":");
            fwData.append("\"");
            fwData.append(entry.getValue());
            fwData.append("\"");
         }
         fwData.append("};");
         fwDataLog.append(fwData.toString());
      }
      logger.info("rmi上传");
      try{
         String res = ost.transfer("3G05", "");
         res = dit.transfer(fwDataLog.toString());
         logger.info(res);
         logger.info("rmi结束上传");
      }catch(Exception e){
         e.printStackTrace();
         logger.info(e.getMessage());
         logger.info("rmi结束上传失败");
      }
   }

   private void writeLog(List<Map<String, Object>> list) throws Exception {
      if(list == null || list.size() == 0){
         return;
      }
      BufferedWriter fw = null;
      try {
         String fileName = zipfileName + ".txt";
         logFilePath = System.getProperty("user.dir") + File.separator + "logdat" + File.separator + zipfileName.substring(0, 8);
         File file = new File(logFilePath);
         if (!file.exists()) {
            file.mkdirs();
         }
         String logFileSonPath = logFilePath + File.separator + fileName;
         File logFile = new File(logFileSonPath);
         if (!logFile.exists()) {
            logFile.createNewFile();
         }
         fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "ISO-8859-1")); // 指定编码格式，以免读取时中文字符异常
         for (Map<String, Object> map : list) {
            StringBuffer fwData = new StringBuffer();
            fwData.append("{");
            for(Map.Entry<String, Object> entry : map.entrySet()){
               if(fwData.length() > 10){
                  fwData.append(",");
               }
               fwData.append("\"");
               fwData.append(entry.getKey());
               fwData.append("\"");
               fwData.append(":");
               fwData.append("\"");
               fwData.append(entry.getValue());
               fwData.append("\"");
            }
            fwData.append("}");
            fw.append(fwData.toString());
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
   
   private void zipLog(String fileName) throws Exception {
      String zipPath = System.getProperty("user.dir") + File.separator + "logdat" + File.separator + fileName.substring(0,8);
      File file = new File(zipPath);
      if(!file.exists()){
         return;
      }
      File[] files = file.listFiles();
      for(int i = 0; i < files.length; i++){
         if(files[i].getName().equals(fileName+".txt")){
            File zipFile = new File(files[i].getPath().replace(".txt", ".zip"));
            boolean isPass = true;
            if(zipFile.exists()){
               isPass = zipFile.delete();
            }
            //logger.info(files[i].getPath().replace(".txt", ".zip")+"日志zip。"+String.valueOf(isPass));
            if(!isPass){
               return;
            }
            String issave = ZipUtil.zip(files[i].getPath(), zipPath + File.separator + files[i].getName().replace(".txt", ".zip"), ZipUtil.PASSWD);
            if(issave != null){
               files[i].delete();
               Properties cfg = new Properties();
               cfg.setProperty("station.id", properties.getProperty("company.stationID"));
               cfg.setProperty("station.name", properties.getProperty("company.stationName"));
               cfg.setProperty("catalog.createTime", DateUtil.lFormat(new Date()));
               cfg.setProperty("catalog.zipTime", DateUtil.lFormat(new Date()));
               cfg.setProperty("catalog.status", Constant.STATUS_ZIP);
               cfg.setProperty("catalog.md5", FileUtil.md5File(issave));
               FileUtil.saveProperties(new File(files[i].getPath().replace(".txt", ".cfg")), cfg, properties.getProperty("company.stationID"));
            }
         }
      }
   }

   public interface LogSize {

      public void getsize(long rsize, long wsize);
   }
}
