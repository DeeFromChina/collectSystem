package com.golead.disManager.ui.dialog;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
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
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

public class LogDialog extends Dialog {

   private static Logger logger = Logger.getLogger(LogDialog.class);

   protected Object      result;
   protected Shell       shell;

   private StyledText    stsTextLog;

   /**
    * Create the dialog.
    * 
    * @param parent
    * @param style
    */
   public LogDialog(Shell parent, int style) {
      super(parent, style);
      setText("系统配置");
   }

   /**
    * Open the dialog.
    * 
    * @return the result
    */
   public Object open(String message) {
      createContents();
      String str = message.replaceAll("###", "\r\n");
      if(str.startsWith("log:start")) str=str.substring(11);
      str=str.substring(0,str.length()-5);
      stsTextLog.setText(str);
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

   /**
    * Create contents of the dialog.
    */
   private void createContents() {
      shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE|SWT.APPLICATION_MODAL);
      shell.setImage(SWTResourceManager.getImage(ConfigDialog.class, "/images/config24.png"));
      shell.setText("日志");
      shell.setSize(800, 500);
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

      stsTextLog = new StyledText(composite_1, SWT.BORDER | SWT.READ_ONLY|SWT.V_SCROLL);
      stsTextLog.setTopMargin(2);
      stsTextLog.setBottomMargin(2);
      stsTextLog.setRightMargin(2);
      stsTextLog.setLeftMargin(2);
   }
}
