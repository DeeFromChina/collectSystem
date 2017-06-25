package com.golead.disManager.ui.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.disManager.communication.CommunicationClient;
import com.golead.disManager.dao.JdbcDao;
import com.golead.disManager.ui.composite.FtpFileComposite;
import com.golead.disManager.ui.composite.GasStationComposite;
import com.golead.disManager.ui.composite.LogComposite;
import com.golead.disManager.ui.composite.StaticComposite;
import com.golead.disManager.ui.dialog.AboutDialog;
import com.golead.disManager.ui.dialog.ConfigDialog;
import com.golead.disManager.util.ResultSetMap;
import com.golead.disManager.util.Util;

public class DisWindow {
   private static Logger                    logger = Logger.getLogger(DisWindow.class);

   protected Shell                          shell;
   private List<ResultSetMap>               companyList;
   private Map<String, CommunicationClient> commClients;

   private Properties                       properties;
   private LogComposite                     logComposite;
   private StaticComposite                  staticComposite;
   private FtpFileComposite                 ftpFileComposite;
   private Composite                        mainComposite;
   private GasStationComposite              gasStationComposite;
   private Display                          display;
   private StackLayout                      stackLayout;
   private TimerThread                      timer;

   public DisWindow(Properties properties) {
      this.properties = properties;
   }

   /**
    * Open the window.
    */
   public void open() {
      display = Display.getDefault();
      loadCompany();
      commClients = new HashMap<String, CommunicationClient>();
      createContents();

      loadComposite(0);

      shell.open();
      shell.layout();

      timer = new TimerThread();
      timer.start();

      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
   }

   private void loadCompany() {
      String sql = "select o.org_code as code,o.org_name as name,o.org_abbr as abbrName,count(o2.org_code) as stationCount from sys_org o,sys_org o1,sys_org o2";
      sql += " where o1.up_org_code=o.org_code and o2.up_org_code=o1.org_code and length(o.org_code)=8 and o2.org_type='Z'";
      sql += " group by o.org_code,o.org_name,o.org_abbr order by o.org_code";
      companyList = JdbcDao.query(sql);
   }

   /**
    * @wbp.parser.entryPoint
    */
   protected void createContents() {
      shell = new Shell();
      shell.setSize(800, 500);
      shell.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/monitor16.png"));
      shell.setMaximized(true);
      shell.setText("国力数据采集监控系统");
      shell.setLayout(new swing2swt.layout.BorderLayout(0, 0));

      ToolBar toolbar = new ToolBar(shell, SWT.FLAT);
      toolbar.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      toolbar.setLayoutData(BorderLayout.NORTH);

      ToolItem btnExit = new ToolItem(toolbar, SWT.NONE);
      btnExit.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int rc = Util.confirm(shell, "退出", "是否真的退出系统？");
            if (rc == SWT.YES) {
               timer.exit();
               while (timer.isAlive()) {
                  try {
                     Thread.sleep(200);
                  } catch (InterruptedException e1) {
                  }
               }
               shell.dispose();
            }
         }
      });
      btnExit.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/exit24.png"));
      btnExit.setText("  退出  ");

      ToolItem btnStation = new ToolItem(toolbar, SWT.NONE);
      btnStation.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadComposite(0);
         }
      });
      btnStation.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/gas24.png"));
      btnStation.setText("加油站");

      ToolItem btnDataLoad = new ToolItem(toolbar, SWT.NONE);
      btnDataLoad.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/load24.png"));
      btnDataLoad.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadComposite(1);
         }
      });
      btnDataLoad.setText("数据加载");

      ToolItem btnStatic = new ToolItem(toolbar, SWT.NONE);
      btnStatic.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/data24.png"));
      btnStatic.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadComposite(3);
         }
      });
      btnStatic.setText("数据统计");

      ToolItem btnLog = new ToolItem(toolbar, SWT.NONE);
      btnLog.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadComposite(2);
         }
      });
      btnLog.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/book24.png"));
      btnLog.setText("系统日志");

      ToolItem btnConfig = new ToolItem(toolbar, SWT.NONE);
      btnConfig.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            ConfigDialog cd = new ConfigDialog(shell, SWT.NONE | SWT.APPLICATION_MODAL);
            cd.open();
         }
      });
      btnConfig.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/config24.png"));
      btnConfig.setText("  配置  ");

      ToolItem btnAbout = new ToolItem(toolbar, SWT.NONE);
      btnAbout.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            AboutDialog cd = new AboutDialog(shell, SWT.NONE | SWT.APPLICATION_MODAL);
            cd.open();
         }
      });
      btnAbout.setImage(SWTResourceManager.getImage(DisWindow.class, "/images/about24.png"));
      btnAbout.setText("关于系统");

      mainComposite = new Composite(shell, SWT.NONE);
      mainComposite.setLayoutData(BorderLayout.CENTER);
      stackLayout = new StackLayout();
      mainComposite.setLayout(stackLayout);
      gasStationComposite = new GasStationComposite(mainComposite, SWT.NONE, this);

      staticComposite = new StaticComposite(mainComposite, SWT.NONE,this);
      ftpFileComposite = new FtpFileComposite(mainComposite, SWT.NONE, this);
      logComposite = new LogComposite(mainComposite, SWT.NONE);
   }

   private void loadTabFolder(int idx) {
      if (idx == 0) gasStationComposite.load();
   }

   private void loadComposite(int idx) {
      if (idx == 0) {
         stackLayout.topControl = gasStationComposite;
         loadTabFolder(0);
      }
      else if (idx == 1) {
         stackLayout.topControl = ftpFileComposite;
         ftpFileComposite.load();
      }
      else if (idx == 2) {
         stackLayout.topControl = logComposite;
         logComposite.load();
      }
      else if (idx == 3) {
         stackLayout.topControl = staticComposite;
         staticComposite.load();
      }
      mainComposite.layout();
   }

   public Properties getProperties() {
      return properties;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public Map<String, CommunicationClient> getCommClients() {
      return commClients;
   }

   public void setCommClients(Map<String, CommunicationClient> commClients) {
      this.commClients = commClients;
   }

   public List<ResultSetMap> getCompanyList() {
      return companyList;
   }

   public void setCompanyList(List<ResultSetMap> companyList) {
      this.companyList = companyList;
   }

   class TimerThread extends Thread {
      private Runnable cmd = new Runnable() {
                              public void run() {
                                 Object object = stackLayout.topControl;
                                 if (object instanceof GasStationComposite) {
                                    GasStationComposite gsc = (GasStationComposite) object;
                                    gsc.loadStation2();
                                 }
                              }
                           };
      private boolean  res = true;

      public void exit() {
         res = false;
      }

      public void run() {
         while (res) {
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
