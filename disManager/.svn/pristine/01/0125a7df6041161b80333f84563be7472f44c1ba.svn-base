package com.golead.disManager.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Util {
   public static Map<String, String> transStatus;
   private static SimpleDateFormat   sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

   public static String getTransStatusName(String key) {
      if (transStatus == null) {
         transStatus = new HashMap<String, String>();
         transStatus.put("collected", "已采集");
         transStatus.put("sent", "已传送");
         transStatus.put("finish", "已加载");
         transStatus.put("fail", "加载失败");
      }
      return transStatus.get(key);
   }

   public static Integer getInt(Object value) {
      if (value == null) return null;
      if (value instanceof Integer || value instanceof BigDecimal) {
         return Integer.valueOf(value.toString());
      }
      else return null;
   }

   public static String getString(Object value) {
      if (value == null) return "";
      if (value instanceof java.util.Date || value instanceof java.sql.Date) {
         return sdf.format(value);
      }
      else return value.toString().trim();
   }

   public static void info(final Display display, final String title, final String message) {
      Runnable runThread = new Runnable() {
         public void run() {
            synchronized (this) {
               MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_INFORMATION | SWT.YES);
               messageBox.setText(title);
               messageBox.setMessage(message);
               messageBox.open();
            }
         }
      };
      display.asyncExec(runThread);
   }

   public static void alert(final Display display, final String title, final String message) {
      final Runnable runThread = new Runnable() {
         public void run() {
            synchronized (this) {
               MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.ICON_WARNING | SWT.YES);
               messageBox.setText(title);
               messageBox.setMessage(message);
               messageBox.open();
            }
         }
      };
      display.asyncExec(runThread);
   }

   public static void info(Shell shell, String title, String message) {
      MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.YES);
      messageBox.setText(title);
      messageBox.setMessage(message);
      messageBox.open();
   }

   public static void alert(Shell shell, String title, String message) {
      MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES);
      messageBox.setText(title);
      messageBox.setMessage(message);
      messageBox.open();
   }

   public static int confirm(Shell shell, String title, String message) {
      MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
      messageBox.setText(title);
      messageBox.setMessage(message);
      return messageBox.open();
   }
}
