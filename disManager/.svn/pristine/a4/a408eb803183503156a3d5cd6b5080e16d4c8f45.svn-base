package com.golead.disManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.golead.disManager.dao.JdbcDao;
import com.golead.disManager.ui.window.DisWindow;

public class DisManager {
   /**
    * @param args
    */
   public static void main(String[] args) {
      try {
         Properties prop = new Properties();
         InputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties");
         prop.load(in);

         JdbcDao.setProperties(prop);
         JdbcDao.openConnection();
         DisWindow window = new DisWindow(prop);
         window.open();
         JdbcDao.closeConnection();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
