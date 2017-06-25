package com.golead.dasManager.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Util {
   public static Map<String, String> transStatus;

   public static String getTransStatusName(String key) {
      if (transStatus == null) {
         transStatus = new HashMap<String, String>();
         transStatus.put("collected", "已采集");
         transStatus.put("zip", "已采集");
         transStatus.put("sent", "已发送");
         transStatus.put("start", "未完成");
         transStatus.put("collectError", "采集错误");
      }
      return transStatus.get(key);
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
