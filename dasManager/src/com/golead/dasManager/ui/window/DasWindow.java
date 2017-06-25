package com.golead.dasManager.ui.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import swing2swt.layout.BorderLayout;

import com.golead.dasManager.swt.SWTResourceManager;
import com.golead.dasManager.ui.composite.FileComposite;
import com.golead.dasManager.ui.composite.LogComposite;
import com.golead.dasManager.ui.dialog.AboutDialog;
import com.golead.dasManager.ui.dialog.ConfigDialog;
import com.golead.dasManager.util.Util;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class DasWindow {

   protected Shell       shell;

   private FileComposite fileComposite;
   private LogComposite  logComposite;
   private boolean       loaded = false;

   /**
    * Open the window.
    */
   public void open() {
      Display display = Display.getDefault();
      createContents();
      loaded = true;
      loadComposite(0);
      shell.open();
      shell.layout();
      setTimer();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
   }

   private void closeWindow() {
      int rc = Util.confirm(shell, "退出", "是否真的退出系统？");
      if (rc == SWT.YES) {
         timer.stopTimer();
         while (timer.isAlive()) {
            try {
               Thread.sleep(200);
            } catch (InterruptedException e1) {
               shell.dispose();
            }
         }
         shell.dispose();
      }
   }

   /**
    * Create contents of the window.
    * 
    * @wbp.parser.entryPoint
    */
   protected void createContents() {
      shell = new Shell();
      shell.addShellListener(new ShellAdapter() {
         @Override
         public void shellClosed(ShellEvent e) {
            closeWindow();
         }
      });
      shell.setSize(900, 530);
      shell.addControlListener(new ControlAdapter() {
         @Override
         public void controlResized(ControlEvent e) {
            resize();
         }
      });
      shell.setImage(SWTResourceManager.getImage(DasWindow.class, "/images/system16.png"));
      shell.setMaximized(true);
      shell.setText("国力数据采集系统");
      shell.setLayout(new BorderLayout(0, 0));

      ToolBar toolBar = new ToolBar(shell, SWT.NONE);
      toolBar.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      toolBar.setLayoutData(BorderLayout.NORTH);

      ToolItem btnExit = new ToolItem(toolBar, SWT.NONE);
      btnExit.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            closeWindow();
         }
      });
      btnExit.setToolTipText("退出系统");
      btnExit.setImage(SWTResourceManager.getImage(DasWindow.class, "/images/exit24.png"));
      btnExit.setText("  退出  ");

      ToolItem btnConfig = new ToolItem(toolBar, SWT.NONE);
      btnConfig.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            ConfigDialog cd = new ConfigDialog(shell, SWT.NONE | SWT.APPLICATION_MODAL);
            cd.open();
         }
      });
      btnConfig.setImage(SWTResourceManager.getImage(DasWindow.class, "/images/config24.png"));
      btnConfig.setText("  配置  ");

      ToolItem btnAbout = new ToolItem(toolBar, SWT.NONE);
      btnAbout.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            AboutDialog cd = new AboutDialog(shell, SWT.NONE | SWT.APPLICATION_MODAL);
            cd.open();
         }
      });
      btnAbout.setImage(SWTResourceManager.getImage(DasWindow.class, "/images/about24.png"));
      btnAbout.setText("关于系统");

      TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
      tabFolder.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            if (!loaded) return;
            int idx = ((TabFolder) e.getSource()).getSelectionIndex();
            loadComposite(idx);
         }
      });

      TabItem tbtmDatalist = new TabItem(tabFolder, SWT.NONE);
      tbtmDatalist.setText(" 数据采集文件 ");
      fileComposite = new FileComposite(tabFolder, SWT.NONE);
      tbtmDatalist.setControl(fileComposite);

      TabItem tbtmLog = new TabItem(tabFolder, SWT.NONE);
      tbtmLog.setText(" 数据采集日志 ");
      logComposite = new LogComposite(tabFolder, SWT.NONE);
      tbtmLog.setControl(logComposite);

      tabFolder.setSelection(tbtmDatalist);
   }

   private void loadComposite(int idx) {
      if (idx == 0) fileComposite.load();
      else if (idx == 1) logComposite.load();
   }

   private void resize() {
      if (fileComposite != null) fileComposite.resize();
   }

   private void setTimer() {
      timer = new TimerThread();
      timer.start();
   }

   private TimerThread timer;

   class TimerThread extends Thread {
      private Runnable cmd     = new Runnable() {
                                  public void run() {
                                     if (fileComposite != null && fileComposite.getVisible()) fileComposite.load();
                                  }
                               };

      private boolean  running = true;

      public void stopTimer() {
         running = false;
      }

      public void run() {
         while (running) {
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
               return;
            }
            shell.getDisplay().asyncExec(cmd);
         }
      }
   }
}
