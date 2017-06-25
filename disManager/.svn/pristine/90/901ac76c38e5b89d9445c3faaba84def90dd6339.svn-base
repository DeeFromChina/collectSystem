package com.golead.disManager.ui.dialog;

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
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.disManager.ui.composite.DateComposite;

public class CollectDialog extends Dialog {
   protected Shell  shell;
   private DateComposite txtDateStart;
   private DateComposite txtDateEnd;
   private String   result = null;

   public CollectDialog(Shell parent, int style) {
      super(parent, style);
      setText("数据采集");
   }

   public String open() {
      createContents();
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
      shell.setText("数据采集");
      shell.setSize(500, 105);
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
            result = null;
            shell.close();
         }
      });
      btnCancel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnCancel.setText("    取消(&C)    ");

      Button btnOk = new Button(composite, SWT.NONE);
      FormData fd_btnOk = new FormData();
      fd_btnOk.right = new FormAttachment(btnCancel, -10);
      fd_btnOk.top = new FormAttachment(0, 5);
      fd_btnOk.bottom = new FormAttachment(100, -5);
      fd_btnOk.width = 80;
      btnOk.setLayoutData(fd_btnOk);
      btnOk.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            String dateStart = String.valueOf(txtDateStart.getYear() * 10000 + (txtDateStart.getMonth() + 1) * 100 + txtDateStart.getDay());
            String dateEnd = String.valueOf(txtDateEnd.getYear() * 10000 + (txtDateEnd.getMonth() + 1) * 100 + txtDateEnd.getDay());
            result = "dateStart:" + dateStart + ",dateEnd:" + dateEnd;
            shell.close();
         }
      });
      btnOk.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
      btnOk.setText("    确定(&O)    ");

      Composite composite_1 = new Composite(shell, SWT.NONE);
      composite_1.setLayoutData(BorderLayout.CENTER);
      composite_1.setLayout(new FormLayout());

      
      txtDateEnd = new DateComposite(composite_1, SWT.NONE);
      FormData fd_txtDateEnd = new FormData();
      fd_txtDateEnd.top = new FormAttachment(0, 5);
      fd_txtDateEnd.left = new FormAttachment(60, 5);
      fd_txtDateEnd.right = new FormAttachment(100, -5);
      txtDateEnd.setLayoutData(fd_txtDateEnd);

      Label lblTo = new Label(composite_1, SWT.CENTER);
      lblTo.setText("至");
      FormData fd_lblTo = new FormData();
      fd_lblTo.top = new FormAttachment(0, 10);
      fd_lblTo.right = new FormAttachment(txtDateEnd, -5);
      fd_lblTo.width = 20;
      lblTo.setLayoutData(fd_lblTo);

      txtDateStart = new DateComposite(composite_1, SWT.NONE);
      FormData fd_txtDateStart = new FormData();
      fd_txtDateStart.top = new FormAttachment(0, 5);
      fd_txtDateStart.right = new FormAttachment(lblTo, 0);
      fd_txtDateStart.left = new FormAttachment(20, 0);
      txtDateStart.setLayoutData(fd_txtDateStart);

      Label lblDate = new Label(composite_1, SWT.NONE);
      lblDate.setText("收集日期：");
      lblDate.setAlignment(SWT.RIGHT);
      FormData fd_lblDate = new FormData();
      fd_lblDate.top = new FormAttachment(0, 10);
      fd_lblDate.right = new FormAttachment(txtDateStart, -5);
      lblDate.setLayoutData(fd_lblDate);

   }
}
