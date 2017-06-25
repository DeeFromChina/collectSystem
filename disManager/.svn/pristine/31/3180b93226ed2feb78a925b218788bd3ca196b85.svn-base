package com.golead.disManager.ui.composite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;

import com.golead.disManager.util.Util;

public class LogStationComposite extends Composite {
   private Shell      shell;
   private String     filePath = System.getProperty("user.dir") + File.separator+"station.properties";
   private StyledText sttLog;
   private Table      table;
   private Text       txtStation;
   private String     stationNo;

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public LogStationComposite(Composite parent, int style) {
      super(parent, style);
      shell = parent.getShell();
      setLayout(new BorderLayout(0, 0));

      Composite composite = new Composite(this, SWT.NONE);
      composite.setLayoutData(BorderLayout.NORTH);
      composite.setLayout(new FormLayout());

      Label lblCollect = new Label(composite, SWT.NONE);
      lblCollect.setAlignment(SWT.RIGHT);
      FormData fd_lblCollect = new FormData();
      fd_lblCollect.top = new FormAttachment(0, 10);
      fd_lblCollect.bottom = new FormAttachment(100, -10);
      fd_lblCollect.left = new FormAttachment(0, 5);
      fd_lblCollect.width = 100;
      lblCollect.setLayoutData(fd_lblCollect);
      lblCollect.setText("加油站编号：");
      
      txtStation = new Text(composite, SWT.BORDER);
      FormData fd_txtStation = new FormData();
      fd_txtStation.top = new FormAttachment(0, 5);
      fd_txtStation.bottom = new FormAttachment(100, -5);
      fd_txtStation.left = new FormAttachment(lblCollect, 5);
      fd_txtStation.width = 130;
      txtStation.setLayoutData(fd_txtStation);
      
      Button btnSearch = new Button(composite, SWT.NONE);
      btnSearch.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            load();
         }
      });
      FormData fd_btnSearch = new FormData();
      fd_btnSearch.left = new FormAttachment(txtStation, 5);
      fd_btnSearch.top = new FormAttachment(0, 5);
      fd_btnSearch.bottom = new FormAttachment(100, -5);
      fd_btnSearch.width = 80;
      btnSearch.setLayoutData(fd_btnSearch);
      btnSearch.setText("    查询   ");
      
      Button btnLogRefreash = new Button(composite, SWT.NONE);
      btnLogRefreash.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            load();
         }
      });
      FormData fd_btnLogRefreash = new FormData();
      fd_btnLogRefreash.left = new FormAttachment(btnSearch, 5);
      fd_btnLogRefreash.top = new FormAttachment(0, 5);
      fd_btnLogRefreash.bottom = new FormAttachment(100, -5);
      fd_btnLogRefreash.width = 80;
      btnLogRefreash.setLayoutData(fd_btnLogRefreash);
      btnLogRefreash.setText("    刷新    ");

      Button btnLogDelete = new Button(composite, SWT.NONE);
      btnLogDelete.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int res = Util.confirm(shell, "删除确认", "是否真的要删除这些日志文件？");
            if (res == SWT.YES) deleteLogs();
            reload();
         }
      });
      FormData fd_btnLogDelete = new FormData();
      fd_btnLogDelete.bottom = new FormAttachment(100, -5);
      fd_btnLogDelete.left = new FormAttachment(btnLogRefreash, 5);
      fd_btnLogDelete.top = new FormAttachment(0, 5);
      btnLogDelete.setLayoutData(fd_btnLogDelete);
      btnLogDelete.setText("  清除日志  ");

      sttLog = new StyledText(this, SWT.BORDER | SWT.V_SCROLL);
      sttLog.setLayoutData(BorderLayout.CENTER);
      sttLog.setTopIndex(Integer.MAX_VALUE);

      table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseDown(MouseEvent e) {
            TableItem item = table.getItem(new Point(e.x, e.y));
            if (item != null){
               if(stationNo!=null){
                  load(item.getText(),stationNo);
               } 
            }
         }
      });

      table.setHeaderVisible(true);
      table.setLayoutData(BorderLayout.WEST);
      table.setLinesVisible(true);

      TableColumn clmnLogName = new TableColumn(table, SWT.NONE);
      clmnLogName.setWidth(200);
      clmnLogName.setText("日志文件");
   }

   private void deleteLogs() {
      TableItem[] items = table.getSelection();
      try {
         for (TableItem item : items) {
            if ("dasService.log".equalsIgnoreCase(item.getText())) {
               Util.alert(shell, "删除提示", "当前使用的日志文件不能删除！");
               continue;
            }
            Properties props = new Properties();
            Properties properties = new Properties();
            InputStreamReader inr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            props.load(inr);
            inr.close();
            inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator+"config.properties"), "UTF-8");
            properties.load(inr);
            inr.close();
            if(props.get(stationNo)==null){
               Util.alert(shell, "提示", "该加油站不存在！");
               return;
            }
            String message = props.getProperty(stationNo).toString();
            String[] messages = message.split(";");
            String company = messages[0].replace("CompanyNo:", "");
            String station = messages[2].replace("GasStationNo:", "");
            String path = properties.getProperty("ftpServer.dasDataDir")==null ? "" : properties.getProperty("ftpServer.dasDataDir").toString();
            
            File file = new File(path+File.separator+company+File.separator+station+File.separator+item.getText());
            boolean res = file.delete();
            if (!res) Util.alert(shell, "删除提示", "日志文件删除失败：" + file.getName());
         }
      }
      catch (Exception e) {
      }
   }

   public void reload() {
      table.removeAll();
      try {
         Properties props = new Properties();
         Properties properties = new Properties();
         InputStreamReader inr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
         props.load(inr);
         inr.close();
         inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator+"config.properties"), "UTF-8");
         properties.load(inr);
         inr.close();
         txtStation.setText(stationNo);
         String message = props.getProperty(txtStation.getText()).toString();
         String[] messages = message.split(";");
         String company = messages[0].replace("CompanyNo:", "");
         String station = messages[2].replace("GasStationNo:", "");
         String path = properties.getProperty("ftpServer.dasDataDir")==null ? "" : properties.getProperty("ftpServer.dasDataDir").toString();
         File files = new File(path+File.separator+company+File.separator+station);
         if(!files.exists()){
            Util.alert(shell, "提示", "找不到该加油站日志！");
            return;
         }
         File[] lists = files.listFiles();
         List<String> list = new ArrayList<String>();
         for (File file : lists) {
            if (!file.getName().startsWith("dasService.log")) continue;
            list.add(file.getName().trim());
         }
         Collections.sort(list, new SortByName2());
         String str = list.get(list.size() - 1);
         list.remove(list.size() - 1);
         list.add(0, str);
         for (String name : list) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name });
         }
         load("dasService.log",stationNo);
      }
      catch (Exception e) {
         e.printStackTrace();
         return;
      }
   }
   
   public void load() {
      table.removeAll();
      try {
         Properties props = new Properties();
         Properties properties = new Properties();
         InputStreamReader inr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
         props.load(inr);
         inr.close();
         inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator+"config.properties"), "UTF-8");
         properties.load(inr);
         inr.close();
         stationNo = txtStation.getText();
         if(props.get(txtStation.getText())==null){
            Util.alert(shell, "提示", "该加油站不存在！");
            return;
         }
         String message = props.getProperty(txtStation.getText()).toString();
         String[] messages = message.split(";");
         String company = messages[0].replace("CompanyNo:", "");
         String station = messages[2].replace("GasStationNo:", "");
         String path = properties.getProperty("ftpServer.dasDataDir")==null ? "" : properties.getProperty("ftpServer.dasDataDir").toString();
         File files = new File(path+File.separator+company+File.separator+station);
         if(!files.exists()){
            Util.alert(shell, "提示", "找不到该加油站日志！");
            return;
         }
         File[] lists = files.listFiles();
         List<String> list = new ArrayList<String>();
         for (File file : lists) {
            if (!file.getName().startsWith("dasService.log")) continue;
            list.add(file.getName().trim());
         }
         Collections.sort(list, new SortByName2());
         String str = list.get(list.size() - 1);
         list.remove(list.size() - 1);
         list.add(0, str);
         for (String name : list) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(new String[] { name });
         }
         load("dasService.log",stationNo);
      }
      catch (Exception e) {
         e.printStackTrace();
         return;
      }
   }

   public void load(String fileName,String stationNo) {
      FileInputStream in;
      sttLog.setText("");
      try {
         Properties props = new Properties();
         Properties properties = new Properties();
         InputStreamReader inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator+"config.properties"), "UTF-8");
         properties.load(inr);
         inr.close();
         inr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
         props.load(inr);
         inr.close();
         String message = props.getProperty(stationNo).toString();
         String[] messages = message.split(";");
         String company = messages[0].replace("CompanyNo:", "");
         String station = messages[2].replace("GasStationNo:", "");
         
         in = new FileInputStream(properties.getProperty("ftpServer.dasDataDir")+File.separator+company+File.separator+station+File.separator + fileName);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
         String tempstr = bufferedReader.readLine();
         while (tempstr != null) {
            sttLog.append(tempstr);
            sttLog.append("\r\n");
            tempstr = bufferedReader.readLine();
         }
         bufferedReader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }
}

class SortByName2 implements Comparator {
   public int compare(Object o1, Object o2) {
      String s1 = (String) o1;
      String s2 = (String) o2;
      return s1.compareTo(s2) == 1 ? 0 : 1;
   }
}
