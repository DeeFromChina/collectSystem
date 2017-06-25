package com.golead.disManager.ui.composite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.golead.common.util.FileUtil;
import com.golead.common.util.NameFilter;
import com.golead.disManager.dao.JdbcDao;
import com.golead.disManager.ui.window.DisWindow;
import com.golead.disManager.util.ResultSetMap;
import com.golead.disManager.util.Util;

public class FtpFileComposite extends Composite {
   private Shell                        shell;
   private String                       filePath     = System.getProperty("user.dir") + File.separator + "station.properties";
   private String                       stationNo;
   private Table                        tblStation;
   private ResultSetMap                 currentCompany;
   private ListViewer                   lstVwCompany;
   private org.eclipse.swt.widgets.List lstCompany;
   private DisWindow                    parentWindow;
   private List<ResultSetMap>           listStations = new ArrayList<ResultSetMap>();
   private ComboViewer                  cbvStation;
   private Combo                        cboStation;
   private TableViewer                  tbvStation;
   private DateComposite                dtStartDate;
   private DateComposite                dtEndDate;
   private SimpleDateFormat             sdf          = new SimpleDateFormat("yyyyMMdd");

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public FtpFileComposite(Composite parent, int style, DisWindow parentWindow) {
      super(parent, style);
      this.parentWindow = parentWindow;
      shell = parent.getShell();
      setLayout(new FormLayout());

      Group group = new Group(this, SWT.NONE);
      group.setLayout(new FillLayout(SWT.HORIZONTAL));
      FormData fd_group = new FormData();
      fd_group.left = new FormAttachment(0, 2);
      fd_group.bottom = new FormAttachment(100, -10);
      fd_group.right = new FormAttachment(100, -2);
      fd_group.top = new FormAttachment(0, 0);
      group.setLayoutData(fd_group);

      SashForm sashForm = new SashForm(group, SWT.NONE);

      lstVwCompany = new ListViewer(sashForm, SWT.BORDER | SWT.V_SCROLL);
      lstVwCompany.setContentProvider(new ArrayContentProvider());
      lstVwCompany.setLabelProvider(new ViewLabelProvider());
      lstCompany = lstVwCompany.getList();
      lstCompany.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            IStructuredSelection selection = (IStructuredSelection) lstVwCompany.getSelection();
            ResultSetMap company = (ResultSetMap) selection.getFirstElement();
            currentCompany = company;
            load(currentCompany);
         }
      });
      lstCompany.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));

      Composite composite_1 = new Composite(sashForm, SWT.NONE);
      composite_1.setLayout(new FormLayout());

      Composite composite = new Composite(composite_1, SWT.NONE);
      FormData fd_composite = new FormData();
      fd_composite.left = new FormAttachment(0, 0);
      fd_composite.height = 36;
      fd_composite.right = new FormAttachment(100, 0);
      fd_composite.top = new FormAttachment(0, 0);
      composite.setLayoutData(fd_composite);
      FormLayout fl_composite = new FormLayout();
      composite.setLayout(fl_composite);

      Label lblCollect = new Label(composite, SWT.NONE);
      lblCollect.setAlignment(SWT.RIGHT);
      FormData fd_lblCollect = new FormData();
      fd_lblCollect.top = new FormAttachment(0, 10);
      fd_lblCollect.bottom = new FormAttachment(100, -10);
      fd_lblCollect.left = new FormAttachment(0, 5);
      fd_lblCollect.width = 60;
      lblCollect.setLayoutData(fd_lblCollect);
      lblCollect.setText("加油站：");

      cbvStation = new ComboViewer(composite, SWT.READ_ONLY);
      cboStation = cbvStation.getCombo();
      cboStation.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            reload();
         }
      });
      FormData fd_cboStation = new FormData();
      fd_cboStation.top = new FormAttachment(0, 5);
      fd_cboStation.left = new FormAttachment(lblCollect, 0);
      fd_cboStation.width = 240;
      cboStation.setLayoutData(fd_cboStation);
      cbvStation.setContentProvider(new ArrayContentProvider());
      cbvStation.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(Object element) {
            if (element instanceof ResultSetMap) {
               ResultSetMap map = (ResultSetMap) element;
               return (map.get("site_code") == null ? "        " : map.get("site_code").toString()) + " " + map.get("org_name").toString();
            }
            else return "";
         }
      });

      Label lblSendTime = new Label(composite, SWT.NONE);
      lblSendTime.setAlignment(SWT.RIGHT);
      FormData fd_lblSendTime = new FormData();
      fd_lblSendTime.width = 70;
      fd_lblSendTime.top = new FormAttachment(0, 10);
      fd_lblSendTime.left = new FormAttachment(cboStation, 5);
      lblSendTime.setLayoutData(fd_lblSendTime);
      lblSendTime.setText("传送时间：");

      dtStartDate = new DateComposite(composite, SWT.NONE);
      dtStartDate.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseUp(MouseEvent e) {
            reload();
         }
      });
      FormData fd_dtStartDate = new FormData();
      fd_dtStartDate.left = new FormAttachment(lblSendTime, 2);
      fd_dtStartDate.top = new FormAttachment(0, 7);
      fd_dtStartDate.height = 20;
      fd_dtStartDate.width = 110;
      dtStartDate.setLayoutData(fd_dtStartDate);

      Label lblTo = new Label(composite, SWT.NONE);
      lblTo.setAlignment(SWT.CENTER);
      FormData fd_lblTo = new FormData();
      fd_lblTo.width = 20;
      fd_lblTo.top = new FormAttachment(0, 10);
      fd_lblTo.left = new FormAttachment(dtStartDate, 0);
      lblTo.setLayoutData(fd_lblTo);
      lblTo.setText("至");

      dtEndDate = new DateComposite(composite, SWT.NONE);
      dtEndDate.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseUp(MouseEvent e) {
            reload();
         }
      });
      FormData fd_dtEndDate = new FormData();
      fd_dtEndDate.left = new FormAttachment(lblTo, 2);
      fd_dtEndDate.top = new FormAttachment(0, 7);
      fd_dtEndDate.height = 20;
      fd_dtEndDate.width = 110;
      dtEndDate.setLayoutData(fd_dtEndDate);

      tbvStation = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
      tblStation = tbvStation.getTable();
      tblStation.setLinesVisible(true);
      tblStation.setHeaderVisible(true);
      FormData fd_tblStation = new FormData();
      fd_tblStation.left = new FormAttachment(0, 2);
      fd_tblStation.bottom = new FormAttachment(100, -2);
      fd_tblStation.right = new FormAttachment(100, -2);
      fd_tblStation.top = new FormAttachment(composite, 0);

      Composite composite_2 = new Composite(composite, SWT.NONE);
      composite_2.setLayout(new FormLayout());
      FormData fd_composite_2 = new FormData();
      fd_composite_2.top = new FormAttachment(0, 0);
      fd_composite_2.bottom = new FormAttachment(100, 0);
      fd_composite_2.left = new FormAttachment(dtEndDate, 0);
      fd_composite_2.right = new FormAttachment(100, 0);
      composite_2.setLayoutData(fd_composite_2);

      Button btnDataLoad = new Button(composite_2, SWT.NONE);
      FormData fd_btnDataLoad = new FormData();
      fd_btnDataLoad.width = 80;
      fd_btnDataLoad.right = new FormAttachment(100, -2);
      fd_btnDataLoad.top = new FormAttachment(0, 5);
      btnDataLoad.setLayoutData(fd_btnDataLoad);
      btnDataLoad.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int i = 0;
            for (TableItem item : tblStation.getItems()) {
               if (item.getChecked()) i++;
            }
            if (i == 0) {
               Util.alert(shell, "提示", "请选择要加载的数据文件。");
               return;
            }
            int res = Util.confirm(shell, "加载确认", "是否真的要加载这些数据文件？");
            if (res == SWT.YES) {
               loadDataToDataCenter();
            }
         }
      });
      btnDataLoad.setText(" 加载文件 ");
      tblStation.setLayoutData(fd_tblStation);

      Button btnLogDelete = new Button(composite_2, SWT.NONE);
      FormData fd_btnLogDelete = new FormData();
      fd_btnLogDelete.width = 80;
      fd_btnLogDelete.top = new FormAttachment(0, 5);
      fd_btnLogDelete.right = new FormAttachment(btnDataLoad, -10);
      btnLogDelete.setLayoutData(fd_btnLogDelete);
      btnLogDelete.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int i = 0;
            for (TableItem item : tblStation.getItems()) {
               if (item.getChecked()) i++;
            }
            if (i == 0) {
               Util.alert(shell, "提示", "请选择要删除的数据文件。");
               return;
            }
            int res = Util.confirm(shell, "删除确认", "是否真的要删除这些日志文件？");
            if (res == SWT.YES) {
               deleteLogFiles();
               reload();
            }
         }
      });
      btnLogDelete.setText(" 清除文件 ");

      TableViewerColumn tblvwcolStation = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblclmnStation = tblvwcolStation.getColumn();
      tblclmnStation.setWidth(200);
      tblclmnStation.setText("数据文件名");

      TableViewerColumn tblvwcolFile = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolStation = tblvwcolFile.getColumn();
      tblcolStation.setWidth(300);
      tblcolStation.setText("加油站");

      TableViewerColumn tblvwcolTransStatus = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolTransStatus = tblvwcolTransStatus.getColumn();
      tblcolTransStatus.setWidth(90);
      tblcolTransStatus.setAlignment(SWT.CENTER);
      tblcolTransStatus.setText("状态");

      TableViewerColumn tblvwcolDate = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolDate = tblvwcolDate.getColumn();
      tblcolDate.setWidth(150);
      tblcolDate.setText("传送时间");

      TableViewerColumn tblvwcolLoadDate = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolLoadDate = tblvwcolLoadDate.getColumn();
      tblcolLoadDate.setWidth(150);
      tblcolLoadDate.setText("加载时间");

      TableViewerColumn tblvwcolMemo = new TableViewerColumn(tbvStation, SWT.NONE);
      TableColumn tblcolMemo = tblvwcolMemo.getColumn();
      tblcolMemo.setWidth(250);
      tblcolMemo.setText("说明");

      sashForm.setWeights(new int[] { 20, 80 });

      tbvStation.setContentProvider(new ArrayContentProvider());
      tbvStation.setLabelProvider(new StationTableLabelProvider());
   }

   private void deleteLogFiles() {
      for (TableItem item : tblStation.getItems()) {
         if (item.getChecked()) {
            Map<String, String> data = (Map<String, String>) item.getData();
            String cfgFile = data.get("cfgPath");
            String datFile = cfgFile.substring(0, cfgFile.length() - 4) + ".dat";
            File cfile = new File(cfgFile);
            cfile.delete();
            File dfile = new File(datFile);
            dfile.delete();
         }
      }
   }

   public void reload() {
      tblStation.removeAll();
      loadFiles(currentCompany, cboStation.getSelectionIndex());
   }

   public void load() {
      lstCompany.removeAll();
      lstVwCompany.setInput(parentWindow.getCompanyList());
   }

   public void load(ResultSetMap company) {
      String sql = "select o.org_name,o.org_code,o.site_code,n.ip_addr,n.port_no";
      sql += " from sys_org o inner join sys_org o1 on o.up_org_code=o1.org_code left join sys_org_net n on o.org_code=n.org_code ";
      sql += " where o.ORG_NAME not like '%发卡点' and o1.up_org_code=" + company.get("code").toString() + " ORDER BY o.ORG_CODE";
      List<ResultSetMap> list = JdbcDao.query(sql);
      if (list != null && list.size() > 0) {
         listStations.clear();
         listStations.addAll(list);
         ResultSetMap map = new ResultSetMap();
         map.put("org_name", "***全部加油站***");
         list.add(0, map);
         cbvStation.setInput(list);
         cboStation.select(0);
      }
      else {
         listStations.clear();
         cboStation.removeAll();
      }
      tblStation.removeAll();
      loadFiles(company, 0);
   }

   private void loadFiles(ResultSetMap company, int index) {
      String filepath = parentWindow.getProperties().getProperty("ftpServer.dasDataDir");
      if (!filepath.endsWith(File.separator)) filepath += File.separator;
      filepath += company.get("code").toString();

      File orgFile = new File(filepath);

      Long sDate = null;
      if (dtStartDate.getDate() != null) {
         Date d = dtStartDate.getDate();
         sDate = Long.parseLong(sdf.format(d) + "000000");
      }

      Long eDate = null;
      if (dtEndDate.getDate() != null) {
         Date d = dtEndDate.getDate();
         eDate = Long.parseLong(sdf.format(d) + "235959");
      }

      if (orgFile == null) return;
      if (!orgFile.isDirectory()) return;

      List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
      if (index == 0) {
         File[] files = orgFile.listFiles();
         for (File f : files) {
            String fname = f.getName();
            String stationPath = filepath + File.separator + fname;
            String st = null;
            for (ResultSetMap map : listStations) {
               if (map.get("site_code") != null && fname.equals(map.get("site_code").toString())) {
                  st = map.get("org_name").toString();
                  break;
               }
            }
            List<Map<String, Object>> list = loadStationFiles(stationPath, st == null ? fname : fname + " " + st, sDate, eDate);
            if (list != null) data.addAll(list);
         }
      }
      else {
         String stationPath = filepath + File.separator + listStations.get(index - 1).get("site_code").toString();
         List<Map<String, Object>> list = loadStationFiles(stationPath, listStations.get(index - 1).get("site_code").toString() + " " + listStations.get(index - 1).get("org_name").toString(), sDate,
               eDate);
         if (list != null) data.addAll(list);
      }
      tbvStation.setInput(data);
   }

   private List<Map<String, Object>> loadStationFiles(String path, String stationName, Long startDate, Long endDate) {
      String filepath = path;
      if (!path.endsWith(File.separator)) filepath += File.separator;
      File sFile = new File(filepath);
      if (!sFile.isDirectory()) return null;

      List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
      File[] files = sFile.listFiles(new NameFilter(".cfg"));
      for (File cfgFile : files) {
         Map<String, Object> map = new HashMap<String, Object>();
         String fileName = cfgFile.getName();
         fileName = fileName.replaceAll("cfg", "dat");

         Properties cfgprops = new Properties();
         try {
            InputStreamReader inSR = new InputStreamReader(new FileInputStream(cfgFile.getPath()), "UTF-8");
            cfgprops.load(inSR);
            inSR.close();
         } catch (IOException e) {
            map.put("memo", "数据文件读取失败。");
         }

         String sendTime = cfgprops.get("catalog.sendTime") == null ? "" : cfgprops.get("catalog.sendTime").toString();
         if (startDate != null) {
            if (sendTime.equals("")) continue;
            Long sTime = Long.parseLong(sendTime);
            if (startDate > sTime) continue;
         }
         if (endDate != null) {
            if (sendTime.equals("")) continue;
            Long sTime = Long.parseLong(sendTime);
            if (endDate < sTime) continue;
         }

         map.put("transDate", sendTime);
         map.put("fileName", fileName);
         map.put("stationName", stationName);
         map.put("cfgPath", cfgFile.getAbsolutePath());

         String loadTime = cfgprops.get("catalog.loadTime") == null ? "" : cfgprops.get("catalog.loadTime").toString();
         map.put("loadDate", loadTime);

         String transStatus = cfgprops.get("catalog.status") == null ? "" : cfgprops.get("catalog.status").toString();
         map.put("transStatus", Util.getTransStatusName(transStatus));
         data.add(map);
      }
      Collections.sort(data, new SortFileName());
      return data;
   }

   public void load(String fileName, String stationNo) {
      FileInputStream in;
      try {
         Properties props = new Properties();
         Properties properties = new Properties();
         InputStreamReader inr = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties"), "UTF-8");
         properties.load(inr);
         inr.close();
         inr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
         props.load(inr);
         inr.close();
         String message = props.getProperty(stationNo).toString();
         String[] messages = message.split(";");
         String company = messages[0].replace("CompanyNo:", "");
         String station = messages[2].replace("GasStationNo:", "");

         in = new FileInputStream(properties.getProperty("ftpServer.dasDataDir") + File.separator + company + File.separator + station + File.separator + fileName);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
         String tempstr = bufferedReader.readLine();
         while (tempstr != null) {
            tempstr = bufferedReader.readLine();
         }
         bufferedReader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void loadDataToDataCenter() {
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File oFile = new File(fileName);
      if (oFile.exists()) {
         Util.alert(shell, "提示", "系统繁忙，请稍后再试！");
         return;
      }
      StringBuffer sb = new StringBuffer("");
      for (TableItem it : tblStation.getItems()) {
         if (it.getChecked()) {
            Map<String, Object> map = (Map<String, Object>) it.getData();
            if (sb.length() > 0) sb.append(";");
            sb.append(map.get("cfgPath").toString());
         }
      }
      Properties props = new Properties();
      props.put("operation", "loadData");
      props.put("file", sb.toString());
      FileUtil.saveProperties(oFile, props);
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }

   class SortFileName implements Comparator<Object> {
      public int compare(Object o1, Object o2) {
         Map<String, Object> s1 = (Map<String, Object>) o1;
         Map<String, Object> s2 = (Map<String, Object>) o2;
         String f1 = s1.get("fileName").toString();
         String f2 = s2.get("fileName").toString();
         return f1.compareTo(f2) * -1;
      }
   }

   class ViewLabelProvider extends LabelProvider implements ILabelProvider {
      public Image getImage(Object element) {
         return null;
      }

      public String getText(Object element) {
         if (element instanceof Map) {
            Map<String, Object> p = (Map<String, Object>) element;
            String label = p.get("code").toString() + " " + p.get("name").toString();
            return label;
         }
         return "";
      }
   }

   class StationTableLabelProvider extends LabelProvider implements ITableLabelProvider {
      public String getColumnText(Object element, int columnIndex) {
         if (element instanceof Map) {
            Map<String, Object> p = (Map<String, Object>) element;
            if (columnIndex == 1) {
               return p.get("stationName") == null ? "" : p.get("stationName").toString();
            }
            else if (columnIndex == 0) {
               return p.get("fileName") == null ? "" : p.get("fileName").toString();
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
            else if (columnIndex == 5) { return p.get("memo") == null ? "" : p.get("memo").toString(); }
         }
         return null;
      }

      public Image getColumnImage(Object element, int columnIndex) {
         return null;
      }
   }
}
