package com.golead.dasManager.ui.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import swing2swt.layout.BorderLayout;

import com.golead.common.util.FileUtil;
import com.golead.dasManager.swt.SWTResourceManager;
import com.golead.dasManager.util.Util;

public class ConfigDialog extends Dialog {

   private static Logger      logger     = Logger.getLogger(ConfigDialog.class);
   protected SimpleDateFormat sdf        = new SimpleDateFormat("yyyyMMddHHmmss");

   protected Object           result;
   protected Shell            shell;
   private Text               txtFtpIp;
   private Text               txtFtpPort;
   private Text               txtFtpUserName;
   private Text               txtFtpPassword;
   private Text               txtDbServer;
   private Text               txtDbPort;
   private Text               txtDbUserName;
   private Text               txtDbPassword;
   private Text               txtStationId;
   private Text               txtStationName;
   private Text               txtDb1;
   private Text               txtCompanyNo;
   private Text               txtCompanyName;
   private Combo              cboSaveDay;
   private Combo              cboStatus;
   private Combo              cboCollectInterval;
   private Combo              cboCollectHour;
   private Combo              cboCollectMinutes;
   private Combo              cboTransformHour;
   private Combo              cboTransformMinutes;
   private Combo              cboCleanHour;
   private Combo              cboCleanMinutes;

   private String[]           statuses   = { "enable", "disable" };
   private String[]           intervals  = { "day" };
   private String[]           minutes    = { "00分", "05分", "10分", "15分", "20分", "25分", "30分", "35分", "40分", "45分", "50分", "55分" };
   private String[]           hours      = { "00点", "01点", "02点", "03点", "04点", "05点", "06点", "07点", "08点", "09点", "10点", "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点", "19点", "20点", "21点",
         "22点", "23点"                   };
   private String[]           saveDays   = { "7天", "15天", "30天", "60天", "90天", "180天" };

   private Properties         properties = new Properties();
   private Group              grpSystem;
   private Text               txtCommPort;

   /**
    * Create the dialog.
    * 
    * @param parent
    * @param style
    */
   public ConfigDialog(Shell parent, int style) {
      super(parent, style);
      setText("系统配置");
   }

   /**
    * Open the dialog.
    * 
    * @return the result
    */
   public Object open() {
      createContents();
      loadProperties();
      shell.open();
      shell.layout();
      Display display = getParent().getDisplay();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
      return result;
   }

   private boolean saveProperties() {
      boolean res;
      String fileName = System.getProperty("user.dir") + File.separator + "config.properties";
      File oFile = new File(fileName);
      try {
         properties.clear();
         properties.put("ftpServer.severIP", txtFtpIp.getText());
         properties.put("ftpServer.port", txtFtpPort.getText());
         properties.put("ftpServer.userName", txtFtpUserName.getText());
         properties.put("ftpServer.password", txtFtpPassword.getText());
         properties.put("server.severName", txtDbServer.getText());
         properties.put("server.port", txtDbPort.getText());
         properties.put("server.userName", txtDbUserName.getText());
         properties.put("server.password", txtDbPassword.getText());
         properties.put("company.stationID", txtStationId.getText());
         properties.put("company.stationName", txtStationName.getText());
         properties.put("server.dbName", txtDb1.getText());
         properties.put("company.companyNo", txtCompanyNo.getText());
         properties.put("company.companyName", txtCompanyName.getText());
         properties.put("comm.port", txtCommPort.getText());

         String str = cboCollectHour.getText() + ":" + cboCollectMinutes.getText();
         str = str.replaceAll("点", "");
         str = str.replaceAll("分", "");
         properties.put("running.dataCollectTime", str);

         str = cboTransformHour.getText() + ":" + cboTransformMinutes.getText();
         str = str.replaceAll("点", "");
         str = str.replaceAll("分", "");
         properties.put("running.dataTransformTime", str);

         properties.put("running.status", statuses[cboStatus.getSelectionIndex()]);
         properties.put("running.timeInterval", intervals[cboCollectInterval.getSelectionIndex()]);

         str = cboSaveDay.getText();
         str = str.replaceAll("天", "");
         properties.put("running.saveDay", str);

         str = cboCleanHour.getText() + ":" + cboCleanMinutes.getText();
         str = str.replaceAll("点", "");
         str = str.replaceAll("分", "");
         properties.put("running.cleanTime", str);

         FileUtil.saveProperties(oFile, properties);
         res = true;
      } catch (Exception e) {
         Util.alert(shell, "提示", "保存配置文件失败。");
         res = false;
      }
      return res;
   }

   private void loadProperties() {
      properties.clear();
      try {
         Properties props = new Properties();
         InputStreamReader inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties"), "UTF-8");
         props.load(inr);
         properties.putAll(props);
         inr.close();

         txtFtpIp.setText(getProperty("ftpServer.severIP"));
         txtFtpPort.setText(getProperty("ftpServer.port"));
         txtFtpUserName.setText(getProperty("ftpServer.userName"));
         txtFtpPassword.setText(getProperty("ftpServer.password"));
         txtDbServer.setText(getProperty("server.severName"));
         txtDbPort.setText(getProperty("server.port"));
         txtDbUserName.setText(getProperty("server.userName"));
         txtDbPassword.setText(getProperty("server.password"));
         txtStationId.setText(getProperty("company.stationID"));
         txtStationName.setText(getProperty("company.stationName"));
         txtDb1.setText(getProperty("server.dbName"));
         txtCompanyNo.setText(getProperty("company.companyNo"));
         txtCompanyName.setText(getProperty("company.companyName"));
         txtCommPort.setText(getProperty("comm.port"));

         String sta = getProperty("running.status");
         cboStatus.select(getIndex(statuses, sta));

         sta = getProperty("running.timeInterval");
         cboCollectInterval.select(getIndex(intervals, sta));

         sta = getProperty("running.dataCollectTime");
         String[] t = sta.split(":");
         int h = (t[0] != null && !"".equals(t[0].trim())) ? new Integer(t[0]) : 0;
         int m = (t.length > 1 && t[1] != null && !"".equals(t[1].trim())) ? new Integer(t[1]) / 5 : 0;
         cboCollectHour.select(new Integer(h));
         cboCollectMinutes.select(new Integer(m));

         sta = getProperty("running.dataTransformTime");
         t = sta.split(":");
         h = (t[0] != null && !"".equals(t[0].trim())) ? new Integer(t[0]) : 0;
         m = (t.length > 1 && t[1] != null && !"".equals(t[1].trim())) ? new Integer(t[1]) / 5 : 0;
         cboTransformHour.select(new Integer(h));
         cboTransformMinutes.select(new Integer(m));

         sta = getProperty("running.saveDay");
         if (sta != null && !"".equals(sta)) {
            String[] items = cboSaveDay.getItems();
            for (int i = 0; i < items.length; i++) {
               if (items[i].equals(sta + "天")) {
                  cboSaveDay.select(i);
                  break;
               }
            }
         }
         sta = getProperty("running.cleanTime");
         t = sta.split(":");
         h = (t[0] != null && !"".equals(t[0].trim())) ? new Integer(t[0]) : 0;
         m = (t.length > 1 && t[1] != null && !"".equals(t[1].trim())) ? new Integer(t[1]) / 5 : 0;
         cboCleanHour.select(new Integer(h));
         cboCleanMinutes.select(new Integer(m));

      } catch (IOException e) {
         Util.alert(shell, "提示", "读取配置文件失败。");
      }
   }

   private int getIndex(String[] items, String value) {
      for (int i = 0; i < items.length; i++) {
         String item = items[i];
         if (item.equalsIgnoreCase(value)) return i;
      }
      return -1;
   }

   private String getProperty(String fieldName) throws UnsupportedEncodingException {
      String p = properties.getProperty(fieldName);
      if (p == null) return "";
      else return p;
   }

   /**
    * Create contents of the dialog.
    */
   private void createContents() {
      shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE);
      shell.setImage(SWTResourceManager.getImage(ConfigDialog.class, "/images/config24.png"));
      shell.setText("系统配置");
      shell.setSize(600, 520);
      shell.setLayout(new BorderLayout(0, 0));
      Rectangle r = Display.getDefault().getBounds();
      int shellH = shell.getBounds().height;
      int shellW = shell.getBounds().width;
      shell.setLocation((r.width - shellW) / 2, (r.height - shellH) / 2);

      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayoutData(BorderLayout.SOUTH);
      composite.setLayout(new FormLayout());

      Button btnApply = new Button(composite, SWT.NONE);
      btnApply.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            saveProperties();
         }
      });
      FormData fd_btnApply = new FormData();
      fd_btnApply.right = new FormAttachment(100, -5);
      fd_btnApply.top = new FormAttachment(0, 5);
      fd_btnApply.bottom = new FormAttachment(100, -5);
      fd_btnApply.width = 80;
      btnApply.setLayoutData(fd_btnApply);
      btnApply.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnApply.setText("    应用(&A)    ");

      Button btnCancel = new Button(composite, SWT.NONE);
      FormData fd_btnCancel = new FormData();
      fd_btnCancel.right = new FormAttachment(btnApply, -10);
      fd_btnCancel.top = new FormAttachment(0, 5);
      fd_btnCancel.bottom = new FormAttachment(100, -5);
      fd_btnCancel.width = 80;
      btnCancel.setLayoutData(fd_btnCancel);
      btnCancel.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            shell.close();
         }
      });
      btnCancel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnCancel.setText("    取消(&C)    ");

      Button btnOk = new Button(composite, SWT.NONE);
      btnOk.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            if (saveProperties()) shell.close();
         }
      });
      btnOk.setImage(null);
      FormData fd_btnOk = new FormData();
      fd_btnOk.top = new FormAttachment(0, 5);
      fd_btnOk.bottom = new FormAttachment(100, -5);
      fd_btnOk.right = new FormAttachment(btnCancel, -10);
      fd_btnOk.width = 80;
      btnOk.setLayoutData(fd_btnOk);
      btnOk.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnOk.setText("    确定(&O)    ");

      Composite composite_1 = new Composite(shell, SWT.NONE);
      composite_1.setLayoutData(BorderLayout.CENTER);
      composite_1.setLayout(new FormLayout());

      Group grpLocal = new Group(composite_1, SWT.NONE);
      grpLocal.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpLocal.setText("  单位信息  ");
      grpLocal.setLayout(new FormLayout());
      FormData fd_grpLocal = new FormData();
      fd_grpLocal.top = new FormAttachment(0, 5);
      fd_grpLocal.right = new FormAttachment(100, -5);
      fd_grpLocal.left = new FormAttachment(0, 5);
      fd_grpLocal.height = 95;
      grpLocal.setLayoutData(fd_grpLocal);
      Label lblCompanyNo = new Label(grpLocal, SWT.NONE);
      lblCompanyNo.setAlignment(SWT.RIGHT);
      FormData fd_lblCompanyNo = new FormData();
      fd_lblCompanyNo.top = new FormAttachment(0, 5);
      fd_lblCompanyNo.left = new FormAttachment(0, 5);
      fd_lblCompanyNo.right = new FormAttachment(20, -5);
      lblCompanyNo.setLayoutData(fd_lblCompanyNo);
      lblCompanyNo.setText("分公司编号");

      Label lblCompanyName = new Label(grpLocal, SWT.NONE);
      lblCompanyName.setAlignment(SWT.RIGHT);
      lblCompanyName.setText("分公司名称");
      FormData fd_lblCompanyName = new FormData();
      fd_lblCompanyName.top = new FormAttachment(0, 5);
      fd_lblCompanyName.left = new FormAttachment(50, 5);
      fd_lblCompanyName.right = new FormAttachment(70, -5);
      lblCompanyName.setLayoutData(fd_lblCompanyName);

      txtCompanyNo = new Text(grpLocal, SWT.BORDER);
      txtCompanyNo.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCompanyNo = new FormData();
      fd_txtCompanyNo.top = new FormAttachment(0, 5);
      fd_txtCompanyNo.left = new FormAttachment(lblCompanyNo, 5);
      fd_txtCompanyNo.right = new FormAttachment(50, -5);
      txtCompanyNo.setLayoutData(fd_txtCompanyNo);

      txtCompanyName = new Text(grpLocal, SWT.BORDER);
      txtCompanyName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCompanyName = new FormData();
      fd_txtCompanyName.top = new FormAttachment(0, 5);
      fd_txtCompanyName.left = new FormAttachment(lblCompanyName, 5);
      fd_txtCompanyName.right = new FormAttachment(100, -5);
      txtCompanyName.setLayoutData(fd_txtCompanyName);

      grpSystem = new Group(composite_1, SWT.NONE);
      grpSystem.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpSystem.setText("  数据采集参数  ");
      grpSystem.setLayout(new FormLayout());
      FormData fd_grpSystem = new FormData();
      fd_grpSystem.left = new FormAttachment(0, 5);
      fd_grpSystem.right = new FormAttachment(100, -5);
      fd_grpSystem.top = new FormAttachment(grpLocal, 5);
      fd_grpSystem.height = 95;
      grpSystem.setLayoutData(fd_grpSystem);

      Label lblStationId = new Label(grpLocal, SWT.NONE);
      lblStationId.setText("加油站编号");
      lblStationId.setAlignment(SWT.RIGHT);
      FormData fd_lblStationId = new FormData();
      fd_lblStationId.top = new FormAttachment(lblCompanyNo, 15);
      fd_lblStationId.right = new FormAttachment(20, -5);
      fd_lblStationId.left = new FormAttachment(0, 5);
      lblStationId.setLayoutData(fd_lblStationId);

      Label lblStatus = new Label(grpSystem, SWT.NONE);
      lblStatus.setText("运行状态");
      lblStatus.setAlignment(SWT.RIGHT);
      FormData fd_lblStatus = new FormData();
      fd_lblStatus.top = new FormAttachment(0, 5);
      fd_lblStatus.right = new FormAttachment(70, -10);
      fd_lblStatus.left = new FormAttachment(50);
      lblStatus.setLayoutData(fd_lblStatus);

      Label lblStationName = new Label(grpLocal, SWT.NONE);
      lblStationName.setText("加油站名称");
      lblStationName.setAlignment(SWT.RIGHT);
      FormData fd_lblStationName = new FormData();
      fd_lblStationName.top = new FormAttachment(lblCompanyName, 15);
      fd_lblStationName.right = new FormAttachment(70, -5);
      fd_lblStationName.left = new FormAttachment(50, 5);
      lblStationName.setLayoutData(fd_lblStationName);

      Label lblCollectInterval = new Label(grpSystem, SWT.NONE);
      lblCollectInterval.setText("数据采集周期");
      lblCollectInterval.setAlignment(SWT.RIGHT);
      FormData fd_lblCollectInterval = new FormData();
      fd_lblCollectInterval.top = new FormAttachment(0, 5);
      fd_lblCollectInterval.right = new FormAttachment(20, -5);
      fd_lblCollectInterval.left = new FormAttachment(0, 5);
      lblCollectInterval.setLayoutData(fd_lblCollectInterval);

      Label lblCollectTime = new Label(grpSystem, SWT.NONE);
      lblCollectTime.setText("数据采集时间");
      lblCollectTime.setAlignment(SWT.RIGHT);
      FormData fd_lblCollectTime = new FormData();
      fd_lblCollectTime.top = new FormAttachment(lblCollectInterval, 13);
      fd_lblCollectTime.right = new FormAttachment(20, -5);
      fd_lblCollectTime.left = new FormAttachment(0, 5);
      lblCollectTime.setLayoutData(fd_lblCollectTime);

      Label lblTransformTime = new Label(grpSystem, SWT.NONE);
      lblTransformTime.setText("数据传送时间");
      lblTransformTime.setAlignment(SWT.RIGHT);
      FormData fd_lblTransformTime = new FormData();
      fd_lblTransformTime.top = new FormAttachment(lblCollectInterval, 13);
      fd_lblTransformTime.left = new FormAttachment(50, 0);
      fd_lblTransformTime.right = new FormAttachment(70, -10);
      lblTransformTime.setLayoutData(fd_lblTransformTime);

      Label saveDay = new Label(grpSystem, SWT.NONE);
      saveDay.setText("文件保留天数");
      saveDay.setAlignment(SWT.RIGHT);
      FormData fd_saveDay = new FormData();
      fd_saveDay.top = new FormAttachment(lblCollectTime, 13);
      fd_saveDay.right = new FormAttachment(20, -5);
      fd_saveDay.left = new FormAttachment(0, 5);
      saveDay.setLayoutData(fd_saveDay);

      Label cleanTime = new Label(grpSystem, SWT.NONE);
      cleanTime.setText("文件清理时间");
      cleanTime.setAlignment(SWT.RIGHT);
      FormData fd_cleanTime = new FormData();
      fd_cleanTime.top = new FormAttachment(lblTransformTime, 13);
      fd_cleanTime.left = new FormAttachment(50, 0);
      fd_cleanTime.right = new FormAttachment(70, -10);
      cleanTime.setLayoutData(fd_cleanTime);

      txtStationId = new Text(grpLocal, SWT.BORDER);
      txtStationId.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtStationId = new FormData();
      fd_txtStationId.top = new FormAttachment(txtCompanyNo, 5);
      fd_txtStationId.right = new FormAttachment(50, -5);
      fd_txtStationId.left = new FormAttachment(lblCompanyNo, 5);
      txtStationId.setLayoutData(fd_txtStationId);

      txtStationName = new Text(grpLocal, SWT.BORDER);
      txtStationName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtStationName = new FormData();
      fd_txtStationName.top = new FormAttachment(txtCompanyNo, 5);
      fd_txtStationName.right = new FormAttachment(100, -5);
      fd_txtStationName.left = new FormAttachment(lblCompanyName, 5);
      txtStationName.setLayoutData(fd_txtStationName);

      Label lblCommPort = new Label(grpLocal, SWT.NONE);
      lblCommPort.setText("数据通讯端口号");
      lblCommPort.setAlignment(SWT.RIGHT);
      FormData fd_lblCommPort = new FormData();
      fd_lblCommPort.top = new FormAttachment(lblStationId, 13);
      fd_lblCommPort.left = new FormAttachment(0, 5);
      fd_lblCommPort.right = new FormAttachment(20, -5);
      lblCommPort.setLayoutData(fd_lblCommPort);

      txtCommPort = new Text(grpLocal, SWT.BORDER);
      txtCommPort.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCommPort = new FormData();
      fd_txtCommPort.top = new FormAttachment(txtStationId, 5);
      fd_txtCommPort.left = new FormAttachment(20, 0);
      fd_txtCommPort.right = new FormAttachment(50, -5);
      txtCommPort.setLayoutData(fd_txtCommPort);

      Group grpDatabase = new Group(composite_1, SWT.NONE);
      grpDatabase.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpDatabase.setText("  本地数据库服务  ");
      grpDatabase.setLayout(new FormLayout());
      FormData fd_grpDatabase = new FormData();
      fd_grpDatabase.left = new FormAttachment(0, 5);
      fd_grpDatabase.right = new FormAttachment(100, -5);
      fd_grpDatabase.top = new FormAttachment(grpSystem, 5);
      fd_grpDatabase.height = 95;
      grpDatabase.setLayoutData(fd_grpDatabase);

      cboStatus = new Combo(grpSystem, SWT.DROP_DOWN | SWT.READ_ONLY);
      FormData fd_cboStatus = new FormData();
      fd_cboStatus.right = new FormAttachment(100, -5);
      fd_cboStatus.left = new FormAttachment(70, 0);
      fd_cboStatus.top = new FormAttachment(0, 5);
      cboStatus.setLayoutData(fd_cboStatus);
      cboStatus.add("启动");
      cboStatus.add("暂停");

      cboCollectInterval = new Combo(grpSystem, SWT.DROP_DOWN | SWT.READ_ONLY);
      FormData fd_cboCollectInterval = new FormData();
      fd_cboCollectInterval.right = new FormAttachment(50, -5);
      fd_cboCollectInterval.left = new FormAttachment(lblCollectInterval, 5);
      fd_cboCollectInterval.top = new FormAttachment(0, 5);
      cboCollectInterval.setLayoutData(fd_cboCollectInterval);
      cboCollectInterval.add("每天");

      cboCollectHour = new Combo(grpSystem, SWT.READ_ONLY);
      cboCollectHour.setItems(hours);
      FormData fd_cboCollectHour = new FormData();
      fd_cboCollectHour.top = new FormAttachment(cboCollectInterval, 5);
      fd_cboCollectHour.left = new FormAttachment(20, 0);
      fd_cboCollectHour.right = new FormAttachment(35, -5);
      cboCollectHour.setLayoutData(fd_cboCollectHour);

      cboCollectMinutes = new Combo(grpSystem, SWT.READ_ONLY);
      cboCollectMinutes.setItems(minutes);
      FormData fd_cboCollectMinutes = new FormData();
      fd_cboCollectMinutes.top = new FormAttachment(cboCollectInterval, 5);
      fd_cboCollectMinutes.left = new FormAttachment(cboCollectHour, 2);
      fd_cboCollectMinutes.right = new FormAttachment(50, -5);
      cboCollectMinutes.setLayoutData(fd_cboCollectMinutes);

      cboTransformHour = new Combo(grpSystem, SWT.READ_ONLY);
      cboTransformHour.setItems(hours);
      FormData fd_cboTransformHour = new FormData();
      fd_cboTransformHour.top = new FormAttachment(cboStatus, 5);
      fd_cboTransformHour.left = new FormAttachment(70);
      fd_cboTransformHour.right = new FormAttachment(85, -5);
      cboTransformHour.setLayoutData(fd_cboTransformHour);

      cboTransformMinutes = new Combo(grpSystem, SWT.READ_ONLY);
      cboTransformMinutes.setItems(minutes);
      FormData fd_cboTransformMinutes = new FormData();
      fd_cboTransformMinutes.top = new FormAttachment(cboStatus, 5);
      fd_cboTransformMinutes.left = new FormAttachment(85);
      fd_cboTransformMinutes.right = new FormAttachment(100, -5);
      cboTransformMinutes.setLayoutData(fd_cboTransformMinutes);

      cboSaveDay = new Combo(grpSystem, SWT.DROP_DOWN | SWT.READ_ONLY);
      cboSaveDay.setItems(saveDays);
      FormData fd_textSaveDay = new FormData();
      fd_textSaveDay.top = new FormAttachment(cboCollectHour, 5);
      fd_textSaveDay.right = new FormAttachment(50, -5);
      fd_textSaveDay.left = new FormAttachment(20);
      cboSaveDay.setLayoutData(fd_textSaveDay);

      cboCleanHour = new Combo(grpSystem, SWT.READ_ONLY);
      cboCleanHour.setItems(hours);
      FormData fd_cboCleanHour = new FormData();
      fd_cboCleanHour.top = new FormAttachment(cboTransformHour, 5);
      fd_cboCleanHour.left = new FormAttachment(70);
      fd_cboCleanHour.right = new FormAttachment(85, -5);
      cboCleanHour.setLayoutData(fd_cboCleanHour);

      cboCleanMinutes = new Combo(grpSystem, SWT.READ_ONLY);
      cboCleanMinutes.setItems(minutes);
      FormData fd_cboCleanMinutes = new FormData();
      fd_cboCleanMinutes.top = new FormAttachment(cboTransformMinutes, 5);
      fd_cboCleanMinutes.left = new FormAttachment(85);
      fd_cboCleanMinutes.right = new FormAttachment(100, -5);
      cboCleanMinutes.setLayoutData(fd_cboCleanMinutes);

      Label lblDbServer = new Label(grpDatabase, SWT.NONE);
      lblDbServer.setText("服务器名称");
      lblDbServer.setAlignment(SWT.RIGHT);
      FormData fd_lblDbServer = new FormData();
      fd_lblDbServer.top = new FormAttachment(0, 10);
      fd_lblDbServer.right = new FormAttachment(20, -10);
      fd_lblDbServer.left = new FormAttachment(0, 5);
      lblDbServer.setLayoutData(fd_lblDbServer);

      Label lblDbPort = new Label(grpDatabase, SWT.NONE);
      lblDbPort.setText("端口号");
      lblDbPort.setAlignment(SWT.RIGHT);
      FormData fd_lblDbPort = new FormData();
      fd_lblDbPort.top = new FormAttachment(0, 10);
      fd_lblDbPort.right = new FormAttachment(70, -10);
      fd_lblDbPort.left = new FormAttachment(50);
      lblDbPort.setLayoutData(fd_lblDbPort);

      Label lblDbUserName = new Label(grpDatabase, SWT.NONE);
      lblDbUserName.setText("用户名");
      lblDbUserName.setAlignment(SWT.RIGHT);
      FormData fd_lblDbUserName = new FormData();
      fd_lblDbUserName.top = new FormAttachment(lblDbServer, 10);
      fd_lblDbUserName.right = new FormAttachment(20, -10);
      fd_lblDbUserName.left = new FormAttachment(0, 5);
      lblDbUserName.setLayoutData(fd_lblDbUserName);

      Label lblDbPassword = new Label(grpDatabase, SWT.NONE);
      lblDbPassword.setText("密码");
      lblDbPassword.setAlignment(SWT.RIGHT);
      FormData fd_lblDbPassword = new FormData();
      fd_lblDbPassword.top = new FormAttachment(lblDbPort, 10);
      fd_lblDbPassword.right = new FormAttachment(70, -10);
      fd_lblDbPassword.left = new FormAttachment(50);
      lblDbPassword.setLayoutData(fd_lblDbPassword);

      txtDbServer = new Text(grpDatabase, SWT.BORDER);
      txtDbServer.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbServer = new FormData();
      fd_txtDbServer.top = new FormAttachment(0, 5);
      fd_txtDbServer.right = new FormAttachment(50, -5);
      fd_txtDbServer.left = new FormAttachment(20);
      txtDbServer.setLayoutData(fd_txtDbServer);

      txtDbPort = new Text(grpDatabase, SWT.BORDER);
      txtDbPort.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbPort = new FormData();
      fd_txtDbPort.top = new FormAttachment(0, 5);
      fd_txtDbPort.right = new FormAttachment(100, -5);
      fd_txtDbPort.left = new FormAttachment(70);
      txtDbPort.setLayoutData(fd_txtDbPort);

      txtDbUserName = new Text(grpDatabase, SWT.BORDER);
      txtDbUserName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbUserName = new FormData();
      fd_txtDbUserName.top = new FormAttachment(txtDbServer, 5);
      fd_txtDbUserName.right = new FormAttachment(50, -5);
      fd_txtDbUserName.left = new FormAttachment(20);
      txtDbUserName.setLayoutData(fd_txtDbUserName);

      txtDbPassword = new Text(grpDatabase, SWT.BORDER);
      txtDbPassword.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbPassword = new FormData();
      fd_txtDbPassword.top = new FormAttachment(txtDbPort, 5);
      fd_txtDbPassword.right = new FormAttachment(100, -5);
      fd_txtDbPassword.left = new FormAttachment(70);
      txtDbPassword.setLayoutData(fd_txtDbPassword);

      Label lblDb1 = new Label(grpDatabase, SWT.NONE);
      lblDb1.setText("数据库文件");
      lblDb1.setAlignment(SWT.RIGHT);
      FormData fd_lblDb1 = new FormData();
      fd_lblDb1.left = new FormAttachment(0, 5);
      fd_lblDb1.right = new FormAttachment(20, -10);
      fd_lblDb1.top = new FormAttachment(lblDbUserName, 10);
      lblDb1.setLayoutData(fd_lblDb1);

      txtDb1 = new Text(grpDatabase, SWT.BORDER);
      txtDb1.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDb1 = new FormData();
      fd_txtDb1.right = new FormAttachment(100, -5);
      fd_txtDb1.left = new FormAttachment(20, 0);
      fd_txtDb1.top = new FormAttachment(txtDbUserName, 5);
      txtDb1.setLayoutData(fd_txtDb1);

      Group grpFtp = new Group(composite_1, SWT.NONE);
      grpFtp.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpFtp.setText("  远程FTP服务  ");
      grpFtp.setLayout(new FormLayout());
      FormData fd_grpFtp = new FormData();
      fd_grpFtp.top = new FormAttachment(grpDatabase, 5);
      fd_grpFtp.right = new FormAttachment(100, -5);
      fd_grpFtp.left = new FormAttachment(0, 5);
      fd_grpFtp.height = 65;
      grpFtp.setLayoutData(fd_grpFtp);

      Label lblFtpIp = new Label(grpFtp, SWT.NONE);
      lblFtpIp.setAlignment(SWT.RIGHT);
      FormData fd_lblFtpIp = new FormData();
      fd_lblFtpIp.top = new FormAttachment(0, 10);
      fd_lblFtpIp.left = new FormAttachment(0, 5);
      fd_lblFtpIp.right = new FormAttachment(20, -10);
      lblFtpIp.setLayoutData(fd_lblFtpIp);
      lblFtpIp.setText("服务器IP");

      Label lblFtpPort = new Label(grpFtp, SWT.NONE);
      lblFtpPort.setAlignment(SWT.RIGHT);
      lblFtpPort.setText("端口号");
      FormData fd_lblFtpPort = new FormData();
      fd_lblFtpPort.top = new FormAttachment(0, 10);
      fd_lblFtpPort.left = new FormAttachment(50, 0);
      fd_lblFtpPort.right = new FormAttachment(70, -10);
      lblFtpPort.setLayoutData(fd_lblFtpPort);

      Label lblFtpUserName = new Label(grpFtp, SWT.NONE);
      lblFtpUserName.setAlignment(SWT.RIGHT);
      lblFtpUserName.setText("用户名");
      FormData fd_lblFtpUserName = new FormData();
      fd_lblFtpUserName.top = new FormAttachment(lblFtpIp, 10);
      fd_lblFtpUserName.left = new FormAttachment(0, 5);
      fd_lblFtpUserName.right = new FormAttachment(20, -10);
      lblFtpUserName.setLayoutData(fd_lblFtpUserName);

      Label lblFtpPassword = new Label(grpFtp, SWT.NONE);
      lblFtpPassword.setAlignment(SWT.RIGHT);
      lblFtpPassword.setText("密码");
      FormData fd_lblFtpPassword = new FormData();
      fd_lblFtpPassword.top = new FormAttachment(lblFtpPort, 10);
      fd_lblFtpPassword.left = new FormAttachment(50, 0);
      fd_lblFtpPassword.right = new FormAttachment(70, -10);
      lblFtpPassword.setLayoutData(fd_lblFtpPassword);

      txtFtpIp = new Text(grpFtp, SWT.BORDER);
      txtFtpIp.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtFtpIp = new FormData();
      fd_txtFtpIp.top = new FormAttachment(0, 5);
      fd_txtFtpIp.left = new FormAttachment(20, 0);
      fd_txtFtpIp.right = new FormAttachment(50, -5);
      txtFtpIp.setLayoutData(fd_txtFtpIp);

      txtFtpPort = new Text(grpFtp, SWT.BORDER);
      txtFtpPort.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtFtpPort = new FormData();
      fd_txtFtpPort.top = new FormAttachment(0, 5);
      fd_txtFtpPort.left = new FormAttachment(70, 0);
      fd_txtFtpPort.right = new FormAttachment(100, -5);
      txtFtpPort.setLayoutData(fd_txtFtpPort);

      txtFtpUserName = new Text(grpFtp, SWT.BORDER);
      txtFtpUserName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtFtpUserName = new FormData();
      fd_txtFtpUserName.top = new FormAttachment(txtFtpIp, 5);
      fd_txtFtpUserName.left = new FormAttachment(20, 0);
      fd_txtFtpUserName.right = new FormAttachment(50, -5);
      txtFtpUserName.setLayoutData(fd_txtFtpUserName);

      txtFtpPassword = new Text(grpFtp, SWT.BORDER);
      txtFtpPassword.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtFtpPassword = new FormData();
      fd_txtFtpPassword.top = new FormAttachment(txtFtpPort, 5);
      fd_txtFtpPassword.left = new FormAttachment(70, 0);
      fd_txtFtpPassword.right = new FormAttachment(100, -5);
      txtFtpPassword.setLayoutData(fd_txtFtpPassword);
   }
}
