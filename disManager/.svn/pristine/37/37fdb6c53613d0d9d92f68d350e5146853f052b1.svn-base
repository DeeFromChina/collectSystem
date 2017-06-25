package com.golead.disManager.ui.dialog;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.disManager.ui.composite.GasStationComposite;
import com.golead.disManager.util.Const;
import com.golead.disManager.util.Util;
import org.eclipse.swt.widgets.Combo;

public class EditGasStationDialog extends Dialog {
   private static Logger       logger = Logger.getLogger(EditGasStationDialog.class);
   protected Shell             shell;
   private String              no;
   private Map<String, Object> map;
   private Text                txtGasStationNo;
   private Text                txtGasStationName;
   private Text                txtGasStationIP;
   private Text                txtGasStationPort;
   private int                 result;
   private GasStationComposite gasStationComposite;
   private Combo               cboStationStaus;

   public GasStationComposite getGasStationComposite() {
      return gasStationComposite;
   }

   public void setGasStationComposite(GasStationComposite gasStationComposite) {
      this.gasStationComposite = gasStationComposite;
   }

   public EditGasStationDialog(Shell parent, int style) {
      super(parent, style);
      setText("加油站配置");
   }

   public int open(Map<String, Object> map) {
      this.no = map.get("no").toString();
      this.map = map;
      createContents();
      load();
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

   private void createContents() {
      shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE);
      shell.setImage(SWTResourceManager.getImage(ConfigDialog.class, "/images/config24.png"));
      shell.setText("加油站信息");
      shell.setSize(350, 250);
      shell.setLayout(new BorderLayout(0, 0));
      Rectangle r = Display.getDefault().getBounds();
      int shellH = shell.getBounds().height;
      int shellW = shell.getBounds().width;
      shell.setLocation((r.width - shellW) / 2, (r.height - shellH) / 2);

      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayoutData(BorderLayout.SOUTH);
      composite.setLayout(new FormLayout());

      Button btnCancel = new Button(composite, SWT.NONE);
      FormData fd_btnCancel = new FormData();
      fd_btnCancel.right = new FormAttachment(100, -10);
      fd_btnCancel.top = new FormAttachment(0, 5);
      fd_btnCancel.bottom = new FormAttachment(100, -5);
      fd_btnCancel.width = 80;
      btnCancel.setLayoutData(fd_btnCancel);
      btnCancel.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            result = 0;
            shell.close();
         }
      });
      btnCancel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnCancel.setText("    取消(&C)    ");

      Button btnOk = new Button(composite, SWT.NONE);
      btnOk.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            if ("".equals(txtGasStationPort.getText()) || "".equals(txtGasStationIP.getText())) {
               Util.alert(shell, "提示", "请填写IP地址和端口号!");
               return;
            }
            saveProperties();
            result = 1;
            shell.close();
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

      Label lblGasStationNo = new Label(composite_1, SWT.NONE);
      lblGasStationNo.setText("编号:");
      lblGasStationNo.setAlignment(SWT.RIGHT);
      FormData fd_lblGasStationNo = new FormData();
      fd_lblGasStationNo.top = new FormAttachment(0, 10);
      fd_lblGasStationNo.left = new FormAttachment(0, 5);
      fd_lblGasStationNo.right = new FormAttachment(25, -5);
      lblGasStationNo.setLayoutData(fd_lblGasStationNo);

      Label lblGasStationName = new Label(composite_1, SWT.NONE);
      lblGasStationName.setText("名称:");
      lblGasStationName.setAlignment(SWT.RIGHT);
      FormData fd_lblGasStationName = new FormData();
      fd_lblGasStationName.top = new FormAttachment(lblGasStationNo, 10);
      fd_lblGasStationName.left = new FormAttachment(0, 5);
      fd_lblGasStationName.right = new FormAttachment(25, -5);
      lblGasStationName.setLayoutData(fd_lblGasStationName);

      Label lblGasStationIP = new Label(composite_1, SWT.NONE);
      lblGasStationIP.setText("IP地址:");
      lblGasStationIP.setAlignment(SWT.RIGHT);
      FormData fd_lblGasStationIP = new FormData();
      fd_lblGasStationIP.top = new FormAttachment(lblGasStationName, 12);
      fd_lblGasStationIP.left = new FormAttachment(0, 5);
      fd_lblGasStationIP.right = new FormAttachment(25, -5);
      lblGasStationIP.setLayoutData(fd_lblGasStationIP);

      Label lblGasStationPort = new Label(composite_1, SWT.NONE);
      lblGasStationPort.setText("端口号:");
      lblGasStationPort.setAlignment(SWT.RIGHT);
      FormData fd_lblGasStationPort = new FormData();
      fd_lblGasStationPort.top = new FormAttachment(lblGasStationIP, 12);
      fd_lblGasStationPort.left = new FormAttachment(0, 5);
      fd_lblGasStationPort.right = new FormAttachment(25, -5);
      lblGasStationPort.setLayoutData(fd_lblGasStationPort);

      Label lblGasStationStatus = new Label(composite_1, SWT.NONE);
      lblGasStationStatus.setText("状态:");
      lblGasStationStatus.setAlignment(SWT.RIGHT);
      FormData fd_lblGasStationStatus = new FormData();
      fd_lblGasStationStatus.top = new FormAttachment(lblGasStationPort, 12);
      fd_lblGasStationStatus.left = new FormAttachment(0, 5);
      fd_lblGasStationStatus.right = new FormAttachment(25, -5);
      lblGasStationStatus.setLayoutData(fd_lblGasStationStatus);

      txtGasStationNo = new Text(composite_1, SWT.NONE);
      txtGasStationNo.setEditable(false);
      txtGasStationNo.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtGasStationNo = new FormData();
      fd_txtGasStationNo.top = new FormAttachment(0, 8);
      fd_txtGasStationNo.left = new FormAttachment(25, 5);
      fd_txtGasStationNo.right = new FormAttachment(100, -5);
      txtGasStationNo.setLayoutData(fd_txtGasStationNo);

      txtGasStationName = new Text(composite_1, SWT.NONE);
      txtGasStationName.setEditable(false);
      txtGasStationName.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtGasStationName = new FormData();
      fd_txtGasStationName.top = new FormAttachment(txtGasStationNo, 8);
      fd_txtGasStationName.left = new FormAttachment(25, 5);
      fd_txtGasStationName.right = new FormAttachment(100, -5);
      txtGasStationName.setLayoutData(fd_txtGasStationName);

      txtGasStationIP = new Text(composite_1, SWT.BORDER);
      txtGasStationIP.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtGasStationIP = new FormData();
      fd_txtGasStationIP.top = new FormAttachment(txtGasStationName, 8);
      fd_txtGasStationIP.left = new FormAttachment(25, 5);
      fd_txtGasStationIP.right = new FormAttachment(100, -10);
      txtGasStationIP.setLayoutData(fd_txtGasStationIP);

      txtGasStationPort = new Text(composite_1, SWT.BORDER);
      txtGasStationPort.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_txtGasStationPort = new FormData();
      fd_txtGasStationPort.top = new FormAttachment(txtGasStationIP, 5);
      fd_txtGasStationPort.left = new FormAttachment(25, 5);
      fd_txtGasStationPort.right = new FormAttachment(100, -10);
      txtGasStationPort.setLayoutData(fd_txtGasStationPort);

      cboStationStaus = new Combo(composite_1, SWT.READ_ONLY);
      cboStationStaus.setItems(new String[] { "停用", "正常" });
      FormData fd_cboStationStaus = new FormData();
      fd_cboStationStaus.left = new FormAttachment(25, 5);
      fd_cboStationStaus.width = 60;
      fd_cboStationStaus.top = new FormAttachment(txtGasStationPort, 5);
      cboStationStaus.setLayoutData(fd_cboStationStaus);
   }

   private void load() {

      txtGasStationNo.setText(Util.getString(map.get("no")));
      txtGasStationName.setText(Util.getString(map.get("stationName")));
      txtGasStationIP.setText(Util.getString(map.get("ip")));
      txtGasStationPort.setText(Util.getString(map.get("port")));
      if ("".equals(txtGasStationPort.getText())) txtGasStationPort.setText(Const.STATION_DEFALUT_PORT);
      int status = Util.getString(map.get("status")).equals("") ? 1 : Integer.parseInt(Util.getString(map.get("status")));
      cboStationStaus.select(status);
   }

   public void saveProperties() {
      map.put("ip", txtGasStationIP.getText());
      map.put("port", txtGasStationPort.getText());
      map.put("status", cboStationStaus.getSelectionIndex());
   }
}
