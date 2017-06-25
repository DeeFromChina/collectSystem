package com.golead.disManager.ui.dialog;

import java.io.File;
import java.io.IOException;
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
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.common.util.FileUtil;
import com.golead.disManager.util.Util;

public class ConfigDialog extends Dialog {

   private static Logger      logger     = Logger.getLogger(ConfigDialog.class);
   protected SimpleDateFormat sdf        = new SimpleDateFormat("yyyyMMddHHmmss");

   protected Object           result;
   protected Shell            shell;
   private Text               txtServerName;
   private Text               txtServerPort;
   private Text               txtServerInst;
   private Text               txtServerUserName;
   private Text               txtServerPassword;
   private Text               txtCenterUserName;
   private Text               txtCenterPassword;
   private Text               txtCleanDays;
   private Text               txtDbDir;
   private Combo              cboStatus;
   private Combo              cboIntervalSecond;

   private String[]           statuses   = { "enable", "disable" };
   private String[]           intervals  = { "60" };

   private Properties         properties = new Properties();

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
      try {
         properties.clear();
         properties.put("server.severName", txtServerName.getText());
         properties.put("server.port", txtServerPort.getText());
         properties.put("server.userName", txtServerUserName.getText());
         properties.put("server.password", txtServerPassword.getText());
         properties.put("server.dbName", txtServerInst.getText());
         properties.put("ftpServer.dasDataDir", txtDbDir.getText());
         properties.put("server.dc.userName", txtCenterUserName.getText());
         properties.put("server.dc.password", txtCenterPassword.getText());
         properties.put("ftpServer.keep.days", txtCleanDays.getText());

         properties.put("running.status", statuses[cboStatus.getSelectionIndex()]);
         properties.put("running.interval.second", intervals[cboIntervalSecond.getSelectionIndex()]);

         res = FileUtil.saveProperties(new File(fileName), properties, "数据加载配置");
      } catch (Exception e) {
         Util.alert(shell, "提示", "保存配置文件失败。");
         res = false;
      } finally {
      }
      return res;
   }

   private void loadProperties() {
      properties.clear();
      try {
         Properties props = FileUtil.loadProperties(new File(System.getProperty("user.dir") + File.separator + "config.properties"));
         properties.putAll(props);

         txtServerName.setText(getProperty("server.severName"));
         txtServerPort.setText(getProperty("server.port"));
         txtServerUserName.setText(getProperty("server.userName"));
         txtServerPassword.setText(getProperty("server.password"));
         txtDbDir.setText(getProperty("ftpServer.dasDataDir"));
         txtServerInst.setText(getProperty("server.dbName"));
         txtCenterUserName.setText(getProperty("server.dc.userName"));
         txtCenterPassword.setText(getProperty("server.dc.password"));
         txtCleanDays.setText(getProperty("ftpServer.keep.days"));

         String sta = getProperty("running.status");
         cboStatus.select(getIndex(statuses, sta));
         sta = getProperty("running.interval.second");
         cboIntervalSecond.select(getIndex(intervals, sta));

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
      shell.setSize(729, 330);
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

      Group grpDatabase = new Group(composite_1, SWT.NONE);
      grpDatabase.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpDatabase.setText("  数据库服务器  ");
      grpDatabase.setLayout(new FormLayout());
      FormData fd_grpDatabase = new FormData();
      fd_grpDatabase.left = new FormAttachment(0, 5);
      fd_grpDatabase.right = new FormAttachment(100, -5);
      fd_grpDatabase.top = new FormAttachment(0, 5);
      fd_grpDatabase.height = 70;
      grpDatabase.setLayoutData(fd_grpDatabase);

      Group grpDataCenter = new Group(composite_1, SWT.NONE);
      grpDataCenter.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpDataCenter.setText("  中心数据库  ");
      grpDataCenter.setLayout(new FormLayout());
      FormData fd_grpDataCenter = new FormData();
      fd_grpDataCenter.left = new FormAttachment(0, 5);
      fd_grpDataCenter.right = new FormAttachment(100, -5);
      fd_grpDataCenter.top = new FormAttachment(grpDatabase, 5);
      fd_grpDataCenter.height = 40;
      grpDataCenter.setLayoutData(fd_grpDataCenter);
      
      Group grpDatapath = new Group(composite_1, SWT.NONE);
      grpDatapath.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
      grpDatapath.setText("  数据加载参数  ");
      grpDatapath.setLayout(new FormLayout());
      FormData fd_grpDatapath = new FormData();
      fd_grpDatapath.left = new FormAttachment(0, 5);
      fd_grpDatapath.right = new FormAttachment(100, -5);
      fd_grpDatapath.top = new FormAttachment(grpDataCenter, 5);
      fd_grpDatapath.height = 70;
      grpDatapath.setLayoutData(fd_grpDatapath);

      Label lblDbServer = new Label(grpDatabase, SWT.NONE);
      lblDbServer.setText("IP地址：");
      lblDbServer.setAlignment(SWT.RIGHT);
      FormData fd_lblDbServer = new FormData();
      fd_lblDbServer.top = new FormAttachment(0, 10);
      fd_lblDbServer.right = new FormAttachment(13, -10);
      fd_lblDbServer.left = new FormAttachment(0, 0);
      lblDbServer.setLayoutData(fd_lblDbServer);

      Label lblDbPort = new Label(grpDatabase, SWT.NONE);
      lblDbPort.setText("端口号：");
      lblDbPort.setAlignment(SWT.RIGHT);
      FormData fd_lblDbPort = new FormData();
      fd_lblDbPort.top = new FormAttachment(0, 10);
      fd_lblDbPort.right = new FormAttachment(46, -10);
      fd_lblDbPort.left = new FormAttachment(33, 0);
      lblDbPort.setLayoutData(fd_lblDbPort);

      Label lblDbName = new Label(grpDatabase, SWT.NONE);
      lblDbName.setText("实例名：");
      lblDbName.setAlignment(SWT.RIGHT);
      FormData fd_lblDbName = new FormData();
      fd_lblDbName.top = new FormAttachment(0, 10);
      fd_lblDbName.right = new FormAttachment(79, -10);
      fd_lblDbName.left = new FormAttachment(66, 0);
      lblDbName.setLayoutData(fd_lblDbName);

      Label lblDbUserName = new Label(grpDatabase, SWT.NONE);
      lblDbUserName.setText("用户名：");
      lblDbUserName.setAlignment(SWT.RIGHT);
      FormData fd_lblDbUserName = new FormData();
      fd_lblDbUserName.top = new FormAttachment(lblDbName, 10);
      fd_lblDbUserName.right = new FormAttachment(13, -10);
      fd_lblDbUserName.left = new FormAttachment(0, 5);
      lblDbUserName.setLayoutData(fd_lblDbUserName);

      Label lblDbPassword = new Label(grpDatabase, SWT.NONE);
      lblDbPassword.setText("密码：");
      lblDbPassword.setAlignment(SWT.RIGHT);
      FormData fd_lblDbPassword = new FormData();
      fd_lblDbPassword.top = new FormAttachment(lblDbName, 10);
      fd_lblDbPassword.right = new FormAttachment(46, -10);
      fd_lblDbPassword.left = new FormAttachment(33);
      lblDbPassword.setLayoutData(fd_lblDbPassword);

      Label lblCtUserName = new Label(grpDataCenter, SWT.NONE);
      lblCtUserName.setText("用户名：");
      lblCtUserName.setAlignment(SWT.RIGHT);
      FormData fd_lblCtUserName = new FormData();
      fd_lblCtUserName.top = new FormAttachment(0, 10);
      fd_lblCtUserName.right = new FormAttachment(13, -10);
      fd_lblCtUserName.left = new FormAttachment(0, 5);
      lblCtUserName.setLayoutData(fd_lblCtUserName);

      Label lblCtPassword = new Label(grpDataCenter, SWT.NONE);
      lblCtPassword.setText("密码：");
      lblCtPassword.setAlignment(SWT.RIGHT);
      FormData fd_lblCtPassword = new FormData();
      fd_lblCtPassword.top = new FormAttachment(0, 10);
      fd_lblCtPassword.right = new FormAttachment(46, -10);
      fd_lblCtPassword.left = new FormAttachment(33);
      lblCtPassword.setLayoutData(fd_lblCtPassword);
      
      txtServerName = new Text(grpDatabase, SWT.BORDER);
      txtServerName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbServer = new FormData();
      fd_txtDbServer.top = new FormAttachment(0, 5);
      fd_txtDbServer.right = new FormAttachment(33, -5);
      fd_txtDbServer.left = new FormAttachment(13,0);
      txtServerName.setLayoutData(fd_txtDbServer);

      txtServerPort = new Text(grpDatabase, SWT.BORDER);
      txtServerPort.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbPort = new FormData();
      fd_txtDbPort.top = new FormAttachment(0, 5);
      fd_txtDbPort.right = new FormAttachment(66, -5);
      fd_txtDbPort.left = new FormAttachment(46,0);
      txtServerPort.setLayoutData(fd_txtDbPort);

      txtServerInst = new Text(grpDatabase, SWT.BORDER);
      txtServerInst.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtServerInst = new FormData();
      fd_txtServerInst.top = new FormAttachment(0, 5);
      fd_txtServerInst.right = new FormAttachment(100, -5);
      fd_txtServerInst.left = new FormAttachment(79,0);
      txtServerInst.setLayoutData(fd_txtServerInst);

      txtServerUserName = new Text(grpDatabase, SWT.BORDER);
      txtServerUserName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbUserName = new FormData();
      fd_txtDbUserName.top = new FormAttachment(txtServerInst, 5);
      fd_txtDbUserName.right = new FormAttachment(33, -5);
      fd_txtDbUserName.left = new FormAttachment(13,0);
      txtServerUserName.setLayoutData(fd_txtDbUserName);

      txtServerPassword = new Text(grpDatabase, SWT.BORDER);
      txtServerPassword.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbPassword = new FormData();
      fd_txtDbPassword.top = new FormAttachment(txtServerInst, 5);
      fd_txtDbPassword.right = new FormAttachment(66, -5);
      fd_txtDbPassword.left = new FormAttachment(46,0);
      txtServerPassword.setLayoutData(fd_txtDbPassword);
      
      txtCenterUserName = new Text(grpDataCenter, SWT.BORDER);
      txtCenterUserName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCtUserName = new FormData();
      fd_txtCtUserName.top = new FormAttachment(0, 5);
      fd_txtCtUserName.right = new FormAttachment(33, -5);
      fd_txtCtUserName.left = new FormAttachment(13,0);
      txtCenterUserName.setLayoutData(fd_txtCtUserName);

      txtCenterPassword = new Text(grpDataCenter, SWT.BORDER);
      txtCenterPassword.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCtPassword = new FormData();
      fd_txtCtPassword.top = new FormAttachment(0, 5);
      fd_txtCtPassword.right = new FormAttachment(66, -5);
      fd_txtCtPassword.left = new FormAttachment(46,0);
      txtCenterPassword.setLayoutData(fd_txtCtPassword);

      Label lblDbDir = new Label(grpDatapath, SWT.NONE);
      lblDbDir.setText("文件位置：");
      lblDbDir.setAlignment(SWT.RIGHT);
      FormData fd_lblDbDir = new FormData();
      fd_lblDbDir.top = new FormAttachment(lblDbUserName, 10);
      fd_lblDbDir.right = new FormAttachment(13, -10);
      fd_lblDbDir.left = new FormAttachment(0, 0);
      lblDbDir.setLayoutData(fd_lblDbDir);

      txtDbDir = new Text(grpDatapath, SWT.BORDER);
      txtDbDir.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtDbDir = new FormData();
      fd_txtDbDir.top = new FormAttachment(txtServerUserName, 5);
      fd_txtDbDir.right = new FormAttachment(100, -5);
      fd_txtDbDir.left = new FormAttachment(13,0);
      txtDbDir.setLayoutData(fd_txtDbDir);

      Label lblStatus = new Label(grpDatapath, SWT.NONE);
      lblStatus.setText("状态：");
      lblStatus.setAlignment(SWT.RIGHT);
      FormData fd_lblStatus = new FormData();
      fd_lblStatus.top = new FormAttachment(lblDbDir, 10);
      fd_lblStatus.right = new FormAttachment(13, -10);
      fd_lblStatus.left = new FormAttachment(0, 0);
      lblStatus.setLayoutData(fd_lblStatus);

      cboStatus = new Combo(grpDatapath, SWT.DROP_DOWN | SWT.READ_ONLY);
      FormData fd_cboStatus = new FormData();
      fd_cboStatus.right = new FormAttachment(33, -5);
      fd_cboStatus.left = new FormAttachment(13);
      fd_cboStatus.top = new FormAttachment(txtDbDir, 5);
      cboStatus.setLayoutData(fd_cboStatus);
      cboStatus.add("启动");
      cboStatus.add("暂停");

      Label lblIntervalSecond = new Label(grpDatapath, SWT.NONE);
      lblIntervalSecond.setText("数据加载间隔：");
      lblIntervalSecond.setAlignment(SWT.RIGHT);
      FormData fd_lblIntervalSecond = new FormData();
      fd_lblIntervalSecond.top = new FormAttachment(txtDbDir, 10);
      fd_lblIntervalSecond.right = new FormAttachment(46, -5);
      fd_lblIntervalSecond.left = new FormAttachment(33, 0);
      lblIntervalSecond.setLayoutData(fd_lblIntervalSecond);

      cboIntervalSecond = new Combo(grpDatapath, SWT.DROP_DOWN | SWT.READ_ONLY);
      FormData fd_cboIntervalSecond = new FormData();
      fd_cboIntervalSecond.right = new FormAttachment(66, -5);
      fd_cboIntervalSecond.left = new FormAttachment(46,0);
      fd_cboIntervalSecond.top = new FormAttachment(txtDbDir, 5);
      cboIntervalSecond.setLayoutData(fd_cboIntervalSecond);
      cboIntervalSecond.add("60秒");

      Label lblCleanDays = new Label(grpDatapath, SWT.NONE);
      lblCleanDays.setText("数据保留天数：");
      lblCleanDays.setAlignment(SWT.RIGHT);
      FormData fd_lblCleanDays = new FormData();
      fd_lblCleanDays.top = new FormAttachment(txtDbDir, 10);
      fd_lblCleanDays.right = new FormAttachment(79, -5);
      fd_lblCleanDays.left = new FormAttachment(66, 0);
      lblCleanDays.setLayoutData(fd_lblCleanDays);

      txtCleanDays = new Text(grpDatapath, SWT.BORDER);
      txtCleanDays.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtCleanDays = new FormData();
      fd_txtCleanDays.top = new FormAttachment(txtDbDir, 5);
      fd_txtCleanDays.right = new FormAttachment(100, -5);
      fd_txtCleanDays.left = new FormAttachment(79,0);
      txtCleanDays.setLayoutData(fd_txtCleanDays);      
   }
}
