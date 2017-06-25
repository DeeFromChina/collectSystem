package com.golead.dasManager;

import org.apache.log4j.Logger;

import com.golead.dasManager.ui.window.DasWindow;

public class DasManager {
   private static Logger logger = Logger.getLogger(DasManager.class);

   /**
    * @param args
    */
   public static void main(String[] args) {
      logger.info("数据采集管理程序dasManager启动。");
      try {
         DasWindow window = new DasWindow();
         window.open();
      } catch (Exception e) {
         e.printStackTrace();
      }
      logger.info("数据采集管理程序dasManager退出。");
      System.exit(0);
   }

}
