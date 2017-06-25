package com.golead.disManager.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;

public class AboutDialog extends Dialog {

   protected Object result;
   protected Shell  shell;

   /**
    * Create the dialog.
    * @param parent
    * @param style
    */
   public AboutDialog(Shell parent, int style) {
      super(parent, style);
      setText("关于系统");
   }

   /**
    * Open the dialog.
    * @return the result
    */
   public Object open() {
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

   /**
    * Create contents of the dialog.
    */
   private void createContents() {
      shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
      shell.setImage(SWTResourceManager.getImage(AboutDialog.class, "/images/monitor16.png"));
      shell.setSize(450, 300);
      shell.setText("关于系统");
      shell.setLayout(new BorderLayout(0, 0));
      Rectangle r=Display.getDefault().getBounds();
      int shellH = shell.getBounds().height;
      int shellW = shell.getBounds().width;
      shell.setLocation((r.width-shellW)/2, (r.height-shellH)/2);
      
      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayoutData(BorderLayout.CENTER);
      composite.setLayout(null);
      
      Label lblNewLabel = new Label(composite, SWT.NONE);
      lblNewLabel.setBounds(113, 66, 226, 25);
      lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 16, SWT.NORMAL));
      lblNewLabel.setText("国力数据采集监控系统   v1.0版");
      
      Label lblNewLabel_1 = new Label(composite, SWT.NONE);
      lblNewLabel_1.setBounds(133, 162, 182, 19);
      lblNewLabel_1.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      lblNewLabel_1.setText("广州市国力计算机科技有限公司");
      
      Composite composite_1 = new Composite(shell, SWT.NONE);
      composite_1.setLayoutData(BorderLayout.SOUTH);
      composite_1.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
      
      Button btnOK = new Button(composite_1, SWT.NONE);
      btnOK.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            shell.close();
         }
      });
      btnOK.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      btnOK.setText("    确定(&O)    ");

   }
}
