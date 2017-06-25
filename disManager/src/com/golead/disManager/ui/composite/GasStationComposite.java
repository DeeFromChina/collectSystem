package com.golead.disManager.ui.composite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.common.util.FileUtil;
import com.golead.common.util.NameFilter;
import com.golead.disManager.communication.CommunicationClient;
import com.golead.disManager.communication.CommunicationClientCallback;
import com.golead.disManager.dao.JdbcDao;
import com.golead.disManager.ui.dialog.ClientFileDialog;
import com.golead.disManager.ui.dialog.CollectDialog;
import com.golead.disManager.ui.dialog.EditGasStationDialog;
import com.golead.disManager.ui.dialog.LogDialog;
import com.golead.disManager.ui.window.DisWindow;
import com.golead.disManager.util.ResultSetMap;
import com.golead.disManager.util.Util;
import org.eclipse.swt.widgets.Combo;

public class GasStationComposite extends Composite {
   private static Logger                    logger           = Logger.getLogger(GasStationComposite.class);

   private Color                            COLOR_BLACK      = this.getDisplay().getSystemColor(SWT.COLOR_BLACK);
   private Color                            COLOR_WHITE      = this.getDisplay().getSystemColor(SWT.COLOR_WHITE);
   private Color                            COLOR_BLUE       = this.getDisplay().getSystemColor(SWT.COLOR_BLUE);
   private Color                            COLOR_GREEN      = this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
   private Color                            COLOR_RED        = this.getDisplay().getSystemColor(SWT.COLOR_RED);
   private Font                             FONT_BOLD        = new Font(this.getDisplay(), "Arial", 9, SWT.BOLD);
   private Font                             FONT_NORMAL      = new Font(this.getDisplay(), "Arial", 9, SWT.NORMAL);
   private Image                            IMAGE_BALL_GRAY  = SWTResourceManager.getImage(GasStationComposite.class, "/images/ball_gray16.png");
   private Image                            IMAGE_BALL_RED   = SWTResourceManager.getImage(GasStationComposite.class, "/images/ball_red16.png");
   private Image                            IMAGE_BALL_GREEN = SWTResourceManager.getImage(GasStationComposite.class, "/images/ball_green16.png");

   protected Shell                          shell;
   private Table                            tblStation;
   private TableViewer                      tbvStation;
   private DisWindow                        parentWindow;
   private org.eclipse.swt.widgets.List     lstCompany;
   private ListViewer                       lstVwCompany;
   private MenuItem                         popEditStation;
   private MenuItem                         popItemConnect;
   private MenuItem                         popItemDisconnect;
   private MenuItem                         popItemCollect;
   private MenuItem                         popItemSend;
   private MenuItem                         popItemCollectAndSend;
   private MenuItem                         popItemFileList;
   private MenuItem                         popItemViewLog;
   private DateComposite                    dtDate;
   private CommunicationClient              commClientTemp;
   private MenuItem                         popItemDataTrans;

   private ResultSetMap                     currentCompany;
   private Map<String, CommunicationClient> commClients;
   private List<Map<String, Object>>        dataStation;

   private Button                           btnChkNormal;

   private Button                           btnChkStop;

   public GasStationComposite(Composite parent, int style, DisWindow parentWindow) {
      super(parent, style);
      this.parentWindow = parentWindow;
      this.commClients = parentWindow.getCommClients();
      shell = parent.getShell();
      setLayout(new FillLayout(SWT.HORIZONTAL));

      Group group = new Group(this, SWT.NONE);
      BorderLayout layout = new BorderLayout(0, 0);
      group.setLayout(layout);

      Composite composite = new Composite(group, SWT.NONE);
      composite.setLayoutData(BorderLayout.NORTH);
      composite.setLayout(new FormLayout());

      Label lbla = new Label(composite, SWT.RIGHT);
      FormData fd_lbla = new FormData();
      fd_lbla.left = new FormAttachment(0, 5);
      fd_lbla.top = new FormAttachment(0, 5);
      fd_lbla.bottom = new FormAttachment(100, -10);
      fd_lbla.width = 90;
      lbla.setLayoutData(fd_lbla);
      lbla.setText("数据传送日期：");

      dtDate = new DateComposite(composite, SWT.NONE);
      dtDate.setDate(new Date());
      dtDate.allowNull(false);
      dtDate.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseUp(MouseEvent e) {
            if (currentCompany != null) loadStation();
         }
      });
      FormData fd_dtDate = new FormData();
      fd_dtDate.left = new FormAttachment(lbla, 2);
      fd_dtDate.top = new FormAttachment(0, 0);
      fd_dtDate.height = 20;
      fd_dtDate.width = 120;
      dtDate.setLayoutData(fd_dtDate);

      Button btnSearch = new Button(composite, SWT.NONE);
      btnSearch.setText("  刷新  ");
      btnSearch.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            if (currentCompany != null) loadStation();
         }
      });
      FormData fd_btnSearch = new FormData();
      fd_btnSearch.left = new FormAttachment(dtDate, 2);
      fd_btnSearch.top = new FormAttachment(0, 0);
      fd_btnSearch.bottom = new FormAttachment(100, -5);
      btnSearch.setLayoutData(fd_btnSearch);

      Button btnTrans = new Button(composite, SWT.NONE);
      btnTrans.setText("  加载数据  ");
      btnTrans.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            transData();
         }
      });
      FormData fd_btnTrans = new FormData();
      fd_btnTrans.left = new FormAttachment(btnSearch, 2);
      fd_btnTrans.top = new FormAttachment(0, 0);
      fd_btnTrans.bottom = new FormAttachment(100, -5);
      btnTrans.setLayoutData(fd_btnTrans);

      btnChkStop = new Button(composite, SWT.CHECK);
      btnChkStop.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadStation();
         }
      });
      FormData fd_btnChkStop = new FormData();
      fd_btnChkStop.top = new FormAttachment(0, 5);
      fd_btnChkStop.right = new FormAttachment(100, -10);
      btnChkStop.setLayoutData(fd_btnChkStop);
      btnChkStop.setText("停用加油站");

      btnChkNormal = new Button(composite, SWT.CHECK);
      btnChkNormal.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            loadStation();
         }
      });
      btnChkNormal.setSelection(true);
      FormData fd_btnChkNomal = new FormData();
      fd_btnChkNomal.top = new FormAttachment(0, 5);
      fd_btnChkNomal.right = new FormAttachment(btnChkStop, -5);
      btnChkNormal.setLayoutData(fd_btnChkNomal);
      btnChkNormal.setText("正常加油站");

      tbvStation = new TableViewer(group, SWT.BORDER | SWT.FULL_SELECTION);
      tblStation = tbvStation.getTable();
      tblStation.setSize(50, 50);
      tblStation.setHeaderVisible(true);
      tblStation.setLinesVisible(true);
      setTableColumns();

      Menu popStation = new Menu(tblStation);
      tblStation.setMenu(popStation);

      popEditStation = new MenuItem(popStation, SWT.NONE);
      popEditStation.setText("配置加油站信息...");

      MenuItem menuItem1 = new MenuItem(popStation, SWT.SEPARATOR);

      popItemConnect = new MenuItem(popStation, SWT.NONE);
      popItemConnect.setText("连接加油站");
      popItemDisconnect = new MenuItem(popStation, SWT.NONE);
      popItemDisconnect.setText("断开连接");

      MenuItem menuItem2 = new MenuItem(popStation, SWT.SEPARATOR);

      popItemCollect = new MenuItem(popStation, SWT.NONE);
      popItemCollect.setText("采集数据");
      popItemSend = new MenuItem(popStation, SWT.NONE);
      popItemSend.setText("传送数据");
      popItemCollectAndSend = new MenuItem(popStation, SWT.NONE);
      popItemCollectAndSend.setText("采集并传送数据");

      MenuItem menuItem3 = new MenuItem(popStation, SWT.SEPARATOR);

      popItemFileList = new MenuItem(popStation, SWT.NONE);
      popItemFileList.setText("查看数据文件...");
      popItemViewLog = new MenuItem(popStation, SWT.NONE);
      popItemViewLog.setText("查看操作日志...");

      setMenuEvent();

      lstVwCompany = new ListViewer(group, SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
      lstVwCompany.setContentProvider(new ArrayContentProvider());
      lstVwCompany.setLabelProvider(new ViewLabelProvider());
      lstCompany = lstVwCompany.getList();
      lstCompany.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            IStructuredSelection selection = (IStructuredSelection) lstVwCompany.getSelection();
            ResultSetMap company = (ResultSetMap) selection.getFirstElement();
            currentCompany = company;
            loadStation();
         }
      });
      lstCompany.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      lstCompany.setLayoutData(BorderLayout.WEST);

   }

   public int saveStationConfig(Map<String, Object> map) {
      String sql = "select o.org_code,o.org_name,n.* from sys_org_net n,sys_org o where o.org_code=n.org_code ";
      sql += " and n.ip_addr='" + Util.getString(map.get("ip")) + "'";
      sql += " and n.org_code<>'" + Util.getString(map.get("no")) + "'";
      List<ResultSetMap> list = JdbcDao.query(sql);
      if (list.size() > 0) {
         Util.alert(shell, "警告", "与" + list.get(0).get("org_name") + "的IP地址冲突，请更换IP地址。");
         return -1;
      }

      sql = "select n.* from sys_org_net n where n.org_code='" + Util.getString(map.get("no") + "'");
      list = JdbcDao.query(sql);
      if (list == null) {
         Util.alert(shell, "提示", "数据库访问错误。");
         return -1;
      }
      if (list.size() > 0) {
         sql = "update sys_org_net set ip_addr='" + Util.getString(map.get("ip")) + "',port_no='" + Util.getString(map.get("port")) + "',";
         sql += " org_status='" + Util.getString(map.get("status") + "' ");
         sql += " where org_code='" + Util.getString(map.get("no") + "'");
      }
      else {
         sql = "insert into sys_org_net(org_code,ip_addr,port_no,org_status,id) values('" + Util.getString(map.get("no")) + "','" + Util.getString(map.get("ip"));
         sql += "','" + Util.getString(map.get("port")) + "','" + Util.getString(map.get("status")) + "',HIBERNATE_SEQUENCE.nextval)";
      }
      return JdbcDao.execute(sql);
   }

   public void load() {
      lstCompany.removeAll();
      lstVwCompany.setInput(parentWindow.getCompanyList());
      dataStation = new ArrayList<Map<String, Object>>();
      tbvStation.setInput(dataStation);
      tbvStation.refresh();
   }

   public void transData() {
      TableItem[] items = tblStation.getSelection();
      if (items.length == 0) {
         Util.info(shell, "提示", "请选择一个要加载的数据文件。");
         return;
      }
      String cfgFileName = "";
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File oFile = new File(fileName);
      if (oFile.exists()) Util.alert(shell, "提示", "系统繁忙，请稍后再试！");
      Properties props = new Properties();
      props.put("operation", "trans");
      for (int i = 0; i < items.length; i++) {
         Map<String, Object> data = (Map<String, Object>) items[i].getData();
         props.put("cfgFile_" + i, data.get("cfgName"));
      }
      FileUtil.saveProperties(oFile, props);
   }

   public void loadStation() {
      if (currentCompany == null) return;
      String sql = "select o.*,n.ip_addr,n.port_no,n.id,n.org_status from sys_org o left join sys_org_net n on o.org_code=n.org_code ";
      sql += " where substr(o.org_code,0,8)='" + currentCompany.get("code").toString() + "' ";
      sql += " and o.org_type='Z' ";
      if (btnChkStop.getSelection() && !btnChkNormal.getSelection()) {
         sql += " and n.org_status='0' ";
      }
      else if (!btnChkStop.getSelection() && btnChkNormal.getSelection()) {
         sql += " and n.org_status='1' ";
      }
      else if (!btnChkStop.getSelection() && !btnChkNormal.getSelection()) {
         sql += " and 1=0 ";
      }

      sql += " ORDER BY o.ORG_CODE";
      List<ResultSetMap> listStations = JdbcDao.query(sql);
      if (listStations == null) {
         Util.alert(shell, "提示", "数据库访问错误。");
         return;
      }

      String filepath = parentWindow.getProperties().getProperty("ftpServer.dasDataDir");
      if (!filepath.endsWith(File.separator)) filepath += File.separator;
      String dayStart = String.valueOf(dtDate.getYear() * 10000 + (dtDate.getMonth() + 1) * 100 + dtDate.getDay());

      List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
      for (ResultSetMap rsMap : listStations) {
         Map<String, Object> map = new HashMap<String, Object>();
         String stationNo = rsMap.get("org_code") == null ? "" : rsMap.get("org_code").toString();
         map.put("no", stationNo);
         String stationName = rsMap.get("org_name") == null ? "" : rsMap.get("org_name").toString();
         map.put("stationName", stationName);
         String siteCode = rsMap.get("site_code") == null ? "" : rsMap.get("site_code").toString();
         map.put("siteCode", siteCode);

         String ip = rsMap.get("ip_addr") == null ? "" : rsMap.get("ip_addr").toString();
         String port = rsMap.get("port_no") == null ? "" : rsMap.get("port_no").toString();
         String status = rsMap.get("org_status") == null ? "" : rsMap.get("org_status").toString();
         map.put("ip", ip);
         map.put("port", port);
         map.put("status", status);
         if (!"".equals(ip)) {
            CommunicationClient cc = parentWindow.getCommClients().get(ip);
            if (cc == null) map.put("linkStatus", "未连接");
            else map.put("linkStatus", "已连接");
         }
         else map.put("linkStatus", "");

         if ("".equals(stationNo)) {
            data.add(map);
            continue;
         }
         File stationFile = new File(filepath + currentCompany.get("code").toString() + File.separator + siteCode);
         if (!stationFile.exists()) {
            data.add(map);
            continue;
         }
         File[] stationFiles = stationFile.listFiles(new NameFilter(".cfg"));
         for (File cfgFile : stationFiles) {
            // String fileName = cfgFile.getName();
            // if (!fileName.startsWith(dayStart)) continue;

            Properties cfgprops = new Properties();
            try {
               InputStreamReader inSR = new InputStreamReader(new FileInputStream(cfgFile.getPath()), "UTF-8");
               cfgprops.load(inSR);
               inSR.close();
            } catch (IOException e) {
               logger.error("数据查询错误。");
            }

            String createTime = cfgprops.get("catalog.createTime") == null ? "" : cfgprops.get("catalog.createTime").toString();
            if (!createTime.startsWith(dayStart)) continue;

            map.put("cfgName", cfgFile.getPath());
            String sendTime = cfgprops.get("catalog.sendTime") == null ? "" : cfgprops.get("catalog.sendTime").toString();
            map.put("transDate", sendTime);

            String unZipTime = cfgprops.get("catalog.unzip.time") == null ? "" : cfgprops.get("catalog.unzip.time").toString();
            map.put("loadDate", unZipTime);

            String transStatus = cfgprops.get("catalog.status") == null ? "" : cfgprops.get("catalog.status").toString();
            map.put("transStatus", Util.getTransStatusName(transStatus));
         }
         data.add(map);
      }
      tblStation.removeAll();
      dataStation.clear();
      dataStation.addAll(data);
      tbvStation.refresh();
   }

   public void loadStation2() {
      if (currentCompany == null) return;

      String filepath = parentWindow.getProperties().getProperty("ftpServer.dasDataDir");
      if (!filepath.endsWith(File.separator)) filepath += File.separator;
      String dayStart = String.valueOf(dtDate.getYear() * 10000 + (dtDate.getMonth() + 1) * 100 + dtDate.getDay());

      for (Map<String, Object> stationMap : dataStation) {
         String ip = stationMap.get("ip") == null ? "" : stationMap.get("ip").toString();
         String stationNo = stationMap.get("stationNo") == null ? "" : stationMap.get("stationNo").toString();
         if (!"".equals(ip)) {
            CommunicationClient cc = parentWindow.getCommClients().get(ip);
            if (cc == null) stationMap.put("linkStatus", "未连接");
            else stationMap.put("linkStatus", "已连接");
         }
         else stationMap.put("linkStatus", "");

         String siteCode = stationMap.get("siteCode") == null ? "" : stationMap.get("siteCode").toString();
         File stationFile = new File(filepath + currentCompany.get("code").toString() + File.separator + siteCode);
         if (!stationFile.exists()) continue;

         File[] stationFiles = stationFile.listFiles(new NameFilter(".cfg"));
         for (File cfgFile : stationFiles) {
            String fileName = cfgFile.getName();
            if (!fileName.startsWith(dayStart)) continue;

            Properties cfgprops = new Properties();
            try {
               InputStreamReader inSR = new InputStreamReader(new FileInputStream(cfgFile.getPath()), "UTF-8");
               cfgprops.load(inSR);
               inSR.close();
            } catch (IOException e) {
               logger.error("数据查询错误。");
            }

            String sendTime = cfgprops.get("catalog.sendTime") == null ? "" : cfgprops.get("catalog.sendTime").toString();
            stationMap.put("transDate", sendTime);

            String unZipTime = cfgprops.get("catalog.unzip.time") == null ? "" : cfgprops.get("catalog.unzip.time").toString();
            stationMap.put("loadDate", unZipTime);

            String transStatus = cfgprops.get("catalog.status") == null ? "" : cfgprops.get("catalog.status").toString();
            stationMap.put("transStatus", Util.getTransStatusName(transStatus));
         }
      }
      tbvStation.refresh();
   }

   private void dealCommand(final String source, final String message) {
      Runnable runThread = new Runnable() {
         public void run() {
            synchronized (this) {
               String[] s = source.split(":");
               String ip = s[0];
               ip = ip.substring(1);
               String port = s[1];
               String[] m = message.split(":");
               String code = m[0];
               String msg = m[1];
               if (code.equalsIgnoreCase(CommunicationClientCallback.COMMAND_CONNECT)) clientConnected(ip, msg);
               else if (code.equalsIgnoreCase(CommunicationClientCallback.COMMAND_DISCONNECT)) clientDisConnected(ip, msg);
               else if (code.equalsIgnoreCase(CommunicationClientCallback.COMMAND_LOG)) {
                  LogDialog logDialog = new LogDialog(shell, SWT.NONE);
                  logDialog.open(message);
               }
               else if (code.equalsIgnoreCase(CommunicationClientCallback.COMMAND_FILE_LIST)) {
                  ClientFileDialog cfd = new ClientFileDialog(shell, SWT.NONE);
                  cfd.open(message);
               }
            }
         }
      };
      getDisplay().asyncExec(runThread);
   }

   private void clientDisConnected(String ip, String message) {
      if (message.startsWith("0")) {
         if (commClients.containsKey(ip)) {
            commClients.remove(ip);
         }
      }
   }

   private void clientConnected(String ip, String message) {
      if (message.startsWith("0")) {
         if (!commClients.containsKey(ip)) {
            commClients.put(ip, commClientTemp);
         }
      }
      else {
         String tip = message.substring(2);
         Util.alert(shell, "连接错误", tip);
      }
   }

   private void setMenuEvent() {
      popEditStation.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            Map<String, Object> maptmp = new HashMap<String, Object>();
            maptmp.putAll(map);
            EditGasStationDialog egd = new EditGasStationDialog(shell, SWT.NONE);
            int result = egd.open(maptmp);
            if (result == 0) return;
            int res = saveStationConfig(maptmp);
            if (res > 0) map.putAll(maptmp);
         }
      });
      popItemConnect.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null) {
               commClient = new CommunicationClient(map.get("ip").toString(), new Integer(map.get("port").toString()), new CommunicationClientCallback() {
                  @Override
                  public void command(String source, String message) {
                     dealCommand(source, message);
                  }
               });
               commClient.start();
               commClientTemp = commClient;
            }
            else if (commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站已连接。");
            }
            else {
               commClient.connect();
            }
         }
      });
      popItemDisconnect.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null) {
               Util.alert(shell, "提示", "加油站未连接。");
               return;
            }
            if (commClient.isOpen()) {
               commClient.close();
               commClients.remove(map.get("id").toString());
               commClient = null;
            }
            else {
               Util.alert(shell, "提示", "加油站未连接。");
            }
         }
      });
      popItemCollect.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            tbvStation.getInput();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null || !commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站还未连接。");
               return;
            }
            CollectDialog collectDialog = new CollectDialog(shell, SWT.NONE);
            String result = collectDialog.open();
            if (result == null) return;
            commClient.send("command:" + CommunicationClientCallback.COMMAND_COLLECT + "," + result);
         }
      });
      popItemSend.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            tbvStation.getInput();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null || !commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站还未连接。");
               return;
            }
            commClient.send("command:" + CommunicationClientCallback.COMMAND_SEND);
         }
      });
      popItemCollectAndSend.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            tbvStation.getInput();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null || !commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站还未连接。");
               return;
            }
            CollectDialog collectDialog = new CollectDialog(shell, SWT.NONE);
            String result = collectDialog.open();
            if (result == null) return;
            commClient.send("command:" + CommunicationClientCallback.COMMAND_COLLECT_SEND + "," + result);
         }
      });
      popItemFileList.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            tbvStation.getInput();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null || !commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站还未连接。");
               return;
            }
            commClient.send("command:" + CommunicationClientCallback.COMMAND_FILE_LIST);
         }
      });
      popItemViewLog.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            TableItem[] tableItems = tblStation.getSelection();
            if (tableItems.length == 0) return;
            Map<String, Object> map = (Map<String, Object>) tableItems[0].getData();
            tbvStation.getInput();
            CommunicationClient commClient = commClients.get(map.get("ip").toString());
            if (commClient == null || !commClient.isOpen()) {
               Util.alert(shell, "提示", "加油站还未连接。");
               return;
            }
            commClient.send("command:" + CommunicationClientCallback.COMMAND_LOG);
         }
      });
   }

   private void setTableColumns() {
      TableViewerColumn tblvwcolNo = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolNo = tblvwcolNo.getColumn();
      tblcolNo.setWidth(140);
      tblcolNo.setText("加油站编号");

      TableViewerColumn tblvwcolStation = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolStation = tblvwcolStation.getColumn();
      tblcolStation.setWidth(230);
      tblcolStation.setText("加油站名称");

      TableViewerColumn tblvwcolTransStatus = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolTransStatus = tblvwcolTransStatus.getColumn();
      tblcolTransStatus.setWidth(90);
      tblcolTransStatus.setAlignment(SWT.CENTER);
      tblcolTransStatus.setText("数据采集状态");

      TableViewerColumn tblvwcolDate = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolDate = tblvwcolDate.getColumn();
      tblcolDate.setWidth(150);
      tblcolDate.setText("传送时间");

      TableViewerColumn tblvwcolLoadDate = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolLoadDate = tblvwcolLoadDate.getColumn();
      tblcolLoadDate.setWidth(150);
      tblcolLoadDate.setText("加载时间");

      TableViewerColumn tblvwcolIp = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolIp = tblvwcolIp.getColumn();
      tblcolIp.setWidth(100);
      tblcolIp.setText("IP地址");

      TableViewerColumn tblvwcolPort = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolPort = tblvwcolPort.getColumn();
      tblcolPort.setWidth(60);
      tblcolPort.setText("端口号");

      TableViewerColumn tblvwcolStatus = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolStatus = tblvwcolStatus.getColumn();
      tblcolStatus.setWidth(90);
      tblcolStatus.setText("网络连接状态");

      TableViewerColumn tblvwcolMemo = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolMemo = tblvwcolMemo.getColumn();
      tblcolMemo.setWidth(300);
      tblcolMemo.setText("说明");

      tbvStation.setContentProvider(new ArrayContentProvider());
      tbvStation.setLabelProvider(new StationTableLabelProvider());

   }

   class ViewLabelProvider extends LabelProvider implements ILabelProvider {
      public Image getImage(Object element) {
         return null;
      }

      public String getText(Object element) {
         if (element instanceof Map) {
            Map<String, Object> p = (Map<String, Object>) element;
            String label = p.get("code").toString() + " " + p.get("name").toString() + "(" + p.get("stationCount").toString() + ")";
            return label;
         }
         return "";
      }
   }

   class StationTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

      @SuppressWarnings("unchecked")
      public String getColumnText(Object element, int columnIndex) {
         if (element instanceof Map) {
            Map<String, Object> p = (Map<String, Object>) element;
            if (columnIndex == 0) {
               return p.get("no") == null ? "" : p.get("no").toString();
            }
            else if (columnIndex == 1) {
               return p.get("stationName") == null ? "" : p.get("stationName").toString();
            }
            else if (columnIndex == 2) {
               return p.get("transStatus") == null ? "" : p.get("transStatus").toString();
            }
            else if (columnIndex == 3) {
               String time = "";
               try {
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                  SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                  if (p.get("transDate") != null && !"".equals(p.get("transDate"))) {
                     Date tDate = sdf.parse(p.get("transDate").toString());
                     time = sdt.format(tDate);
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
               return time;
            }
            else if (columnIndex == 4) {
               String time = "";
               try {
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                  SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                  if (p.get("loadDate") != null && !"".equals(p.get("loadDate").toString())) {
                     Date tDate = sdf.parse(p.get("loadDate").toString());
                     time = sdt.format(tDate);
                  }
               } catch (Exception e) {
                  e.printStackTrace();
               }
               return time;
            }
            else if (columnIndex == 5) {
               return p.get("ip") == null ? "" : p.get("ip").toString();
            }
            else if (columnIndex == 6) {
               return p.get("port") == null ? "" : p.get("port").toString();
            }
            else if (columnIndex == 7) {
               return p.get("linkStatus") == null ? "" : p.get("linkStatus").toString();
            }
            else if (columnIndex == 8) { return p.get("siteCode") == null ? "" : p.get("siteCode").toString(); }
         }
         return null;
      }

      public Image getColumnImage(Object element, int columnIndex) {
         Map<String, Object> p = (Map<String, Object>) element;
         if (columnIndex == 7) {
            if (p.get("linkStatus") != null && !"".equals(p.get("linkStatus").toString().trim())) {
               return p.get("linkStatus").toString().equals("已连接") ? IMAGE_BALL_GREEN : IMAGE_BALL_GRAY;
            }
            else return null;
         }
         else return null;
      }

      @Override
      public Color getBackground(Object arg0, int arg1) {
         // Map<String, Object> p = (Map<String, Object>) arg0;
         // return (p.get("linkStatus") != null &&
         // p.get("linkStatus").toString().equals("已连接")) ? COLOR_BLUE :
         // COLOR_WHITE;
         return null;
      }

      @Override
      public Color getForeground(Object arg0, int arg1) {
         Map<String, Object> p = (Map<String, Object>) arg0;
         if (arg1 == 7) {
            return (p.get("linkStatus") != null && p.get("linkStatus").toString().equals("已连接")) ? COLOR_GREEN : COLOR_BLACK;
         }
         else return COLOR_BLACK;
      }

      @Override
      public Font getFont(Object arg0, int arg1) {
         Map<String, Object> p = (Map<String, Object>) arg0;
         if (arg1 == 7) {
            return (p.get("linkStatus") != null && p.get("linkStatus").toString().equals("已连接")) ? FONT_BOLD : FONT_NORMAL;
         }
         else return FONT_NORMAL;
      }
   }
}
