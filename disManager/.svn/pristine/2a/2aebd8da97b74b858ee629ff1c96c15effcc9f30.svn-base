package com.golead.disManager.ui.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

public class ClientFileDialog extends Dialog {

   private static Logger    logger = Logger.getLogger(ClientFileDialog.class);

   protected Object         result;
   protected Shell          shell;
   private Table            tblFile;

   private SimpleDateFormat sdf1   = new SimpleDateFormat("yyyyMMddhhmmss");
   private SimpleDateFormat sdf2   = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

   /**
    * Create the dialog.
    * 
    * @param parent
    * @param style
    */
   public ClientFileDialog(Shell parent, int style) {
      super(parent, style);
      setText("加油站数据文件");
   }

   /**
    * Open the dialog.
    * 
    * @return the result
    */
   public Object open(String message) {
      createContents();
      shell.open();
      shell.layout();
      tblFile.clearAll();
      String msg = message;
      if (message.startsWith("fileList")) {
         msg = message.substring(11);
      }

      String[] rows = msg.split("###");
      for (String row : rows) {
         TableItem item = new TableItem(tblFile, SWT.NULL);
         String[] s = row.split(";");
         String[] res = new String[4];
         res[0] = s[0];
         res[3] = s[3];
         try {
            res[1] = sdf2.format(sdf1.parse(s[1]));
         } catch (ParseException e) {
            res[1] = "";
         }
         try {
            res[2] = sdf2.format(sdf1.parse(s[2]));
         } catch (ParseException e) {
            res[2] = "";
         }

         item.setText(res);
      }
      Display display = getParent().getDisplay();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
      return result;
   }

   /**
    * Create contents of the dialog.
    */
   private void createContents() {
      shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
      shell.setImage(SWTResourceManager.getImage(ConfigDialog.class, "/images/config24.png"));
      shell.setText("加油站数据文件");
      shell.setSize(750, 500);
      shell.setLayout(new BorderLayout(0, 0));
      Rectangle r = Display.getDefault().getBounds();
      int shellH = shell.getBounds().height;
      int shellW = shell.getBounds().width;
      shell.setLocation((r.width - shellW) / 2, (r.height - shellH) / 2);

      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayoutData(BorderLayout.SOUTH);
      composite.setLayout(new FormLayout());

      Button btnOk = new Button(composite, SWT.NONE);
      btnOk.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            shell.close();
         }
      });
      btnOk.setImage(null);
      FormData fd_btnOk = new FormData();
      fd_btnOk.right = new FormAttachment(100, -5);
      fd_btnOk.top = new FormAttachment(0, 5);
      fd_btnOk.bottom = new FormAttachment(100, -5);
      fd_btnOk.width = 80;
      btnOk.setLayoutData(fd_btnOk);
      btnOk.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnOk.setText("    关闭(&C)    ");

      Composite composite_1 = new Composite(shell, SWT.NONE);
      composite_1.setLayoutData(BorderLayout.CENTER);
      composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

      tblFile = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
      tblFile.setHeaderVisible(true);
      tblFile.setLinesVisible(true);

      TableColumn tblclmnFileName = new TableColumn(tblFile, SWT.NONE);
      tblclmnFileName.setWidth(300);
      tblclmnFileName.setText("文件名");

      TableColumn tblclmnCollectTime = new TableColumn(tblFile, SWT.NONE);
      tblclmnCollectTime.setWidth(150);
      tblclmnCollectTime.setText("数据采集时间");

      TableColumn tblclmnSendTime = new TableColumn(tblFile, SWT.NONE);
      tblclmnSendTime.setWidth(150);
      tblclmnSendTime.setText("数据传送时间");

      TableColumn tblclmnStatus = new TableColumn(tblFile, SWT.NONE);
      tblclmnStatus.setWidth(100);
      tblclmnStatus.setText("状态");
   }
}
