package com.golead.disManager.ui.composite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.golead.common.util.FileUtil;
import com.golead.common.util.NameFilter;
import com.golead.disManager.dao.JdbcDao;
import com.golead.disManager.ui.window.DisWindow;
import com.golead.disManager.util.ResultSetMap;
import com.golead.disManager.util.Util;

public class StaticComposite extends Composite {
   private Shell             shell;
   private Composite         cmpsStatic;
   private Composite         compData;
   private DateComposite     dtStartDate;
   private DateComposite     dtEndDate;
   private SimpleDateFormat  sdf = new SimpleDateFormat("yyyyMMdd");
   private ComboViewer       cbvStation;
   private Combo             cboStation;
   private DisWindow         parentWindow;
   private ScrolledComposite scrolledComposite;
   private Label             lbl2sum;
   private Label             lbl3sum;
   private Label             lbl4sum;
   private Label             lbl5sum;

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public StaticComposite(Composite parent, int style, DisWindow parentWindow) {
      super(parent, style);
      this.parentWindow = parentWindow;
      setLayout(new FillLayout(SWT.HORIZONTAL));

      TabFolder tabFolder = new TabFolder(this, SWT.NONE);

      TabItem tbtm1 = new TabItem(tabFolder, SWT.NONE);
      tbtm1.setText("  数据加载情况统计  ");

      Group group1 = new Group(tabFolder, SWT.NONE);
      tbtm1.setControl(group1);
      group1.setLayout(new BorderLayout(0, 0));

      Composite composite1 = new Composite(group1, SWT.NONE);
      composite1.setLayoutData(BorderLayout.NORTH);
      composite1.setLayout(new FormLayout());

      Label lblCollect = new Label(composite1, SWT.NONE);
      lblCollect.setAlignment(SWT.RIGHT);
      FormData fd_lblCollect = new FormData();
      fd_lblCollect.top = new FormAttachment(0, 10);
      fd_lblCollect.bottom = new FormAttachment(100, -10);
      fd_lblCollect.left = new FormAttachment(10, 2);
      fd_lblCollect.width = 60;
      lblCollect.setLayoutData(fd_lblCollect);
      lblCollect.setText("公司名称：");

      cbvStation = new ComboViewer(composite1, SWT.READ_ONLY);
      cboStation = cbvStation.getCombo();
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
               return map.get("name").toString();
            }
            else return "";
         }
      });

      Label lblSendTime = new Label(composite1, SWT.NONE);
      lblSendTime.setAlignment(SWT.RIGHT);
      FormData fd_lblSendTime = new FormData();
      fd_lblSendTime.width = 70;
      fd_lblSendTime.top = new FormAttachment(0, 10);
      fd_lblSendTime.left = new FormAttachment(cboStation, 5);
      lblSendTime.setLayoutData(fd_lblSendTime);
      lblSendTime.setText("传送时间：");

      dtStartDate = new DateComposite(composite1, SWT.NONE);
      FormData fd_dtStartDate = new FormData();
      fd_dtStartDate.left = new FormAttachment(lblSendTime, 2);
      fd_dtStartDate.top = new FormAttachment(0, 7);
      fd_dtStartDate.height = 20;
      fd_dtStartDate.width = 110;
      dtStartDate.setLayoutData(fd_dtStartDate);

      Label lblTo = new Label(composite1, SWT.NONE);
      lblTo.setAlignment(SWT.CENTER);
      FormData fd_lblTo = new FormData();
      fd_lblTo.width = 20;
      fd_lblTo.top = new FormAttachment(0, 10);
      fd_lblTo.left = new FormAttachment(dtStartDate, 0);
      lblTo.setLayoutData(fd_lblTo);
      lblTo.setText("至");

      dtEndDate = new DateComposite(composite1, SWT.NONE);
      FormData fd_dtEndDate = new FormData();
      fd_dtEndDate.left = new FormAttachment(lblTo, 2);
      fd_dtEndDate.top = new FormAttachment(0, 7);
      fd_dtEndDate.height = 20;
      fd_dtEndDate.width = 110;
      dtEndDate.setLayoutData(fd_dtEndDate);

      Button btnStatic = new Button(composite1, SWT.NONE);
      btnStatic.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            query();
         }
      });
      FormData fd_btnStatic = new FormData();
      fd_btnStatic.left = new FormAttachment(dtEndDate, 2);
      fd_btnStatic.top = new FormAttachment(0, 5);
      btnStatic.setLayoutData(fd_btnStatic);
      btnStatic.setText("    统计    ");

      cmpsStatic = new Composite(group1, SWT.NONE);
      cmpsStatic.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      cmpsStatic.setLayoutData(BorderLayout.CENTER);
      cmpsStatic.setLayout(new FormLayout());

      Label lblNewLabel = new Label(cmpsStatic, SWT.NONE);
      lblNewLabel.setText("sss");
      lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
      FormData fd_lblNewLabel = new FormData();
      fd_lblNewLabel.top = new FormAttachment(0, 10);
      fd_lblNewLabel.left = new FormAttachment(10, 0);
      fd_lblNewLabel.right = new FormAttachment(90, 0);
      fd_lblNewLabel.height = 2;
      lblNewLabel.setLayoutData(fd_lblNewLabel);

      Label lbl1 = new Label(cmpsStatic, SWT.SHADOW_NONE);
      lbl1.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lbl1 = new FormData();
      fd_lbl1.left = new FormAttachment(10, 10);
      fd_lbl1.right = new FormAttachment(38, 0);
      fd_lbl1.top = new FormAttachment(lblNewLabel, 5);
      lbl1.setLayoutData(fd_lbl1);
      lbl1.setText("单位名称");

      Label lbl2 = new Label(cmpsStatic, SWT.RIGHT);
      // lbl2.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
      lbl2.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lbl2 = new FormData();
      fd_lbl2.left = new FormAttachment(38, 0);
      fd_lbl2.right = new FormAttachment(51, 0);
      fd_lbl2.top = new FormAttachment(lblNewLabel, 5);
      lbl2.setLayoutData(fd_lbl2);
      lbl2.setText("接收文件(个)");

      Label lbl3 = new Label(cmpsStatic, SWT.RIGHT);
      lbl3.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lbl3 = new FormData();
      fd_lbl3.left = new FormAttachment(51, 0);
      fd_lbl3.right = new FormAttachment(64, 0);
      fd_lbl3.top = new FormAttachment(lblNewLabel, 5);
      lbl3.setLayoutData(fd_lbl3);
      lbl3.setText("加载文件(个)");

      Label lbl4 = new Label(cmpsStatic, SWT.RIGHT);
      lbl4.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lbl4 = new FormData();
      fd_lbl4.left = new FormAttachment(64, 0);
      fd_lbl4.right = new FormAttachment(77, 0);
      fd_lbl4.top = new FormAttachment(lblNewLabel, 5);
      lbl4.setLayoutData(fd_lbl4);
      lbl4.setText("加载成功(个)");

      Label lbl5 = new Label(cmpsStatic, SWT.RIGHT);
      lbl5.setText("加载失败(个)");
      lbl5.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lbl5 = new FormData();
      fd_lbl5.left = new FormAttachment(77, 0);
      fd_lbl5.right = new FormAttachment(90, 0);
      fd_lbl5.top = new FormAttachment(lblNewLabel, 5);
      lbl5.setLayoutData(fd_lbl5);

      Label lblNewLabel1 = new Label(cmpsStatic, SWT.NONE);
      lblNewLabel1.setText("sss");
      lblNewLabel1.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
      FormData fd_lblNewLabel1 = new FormData();
      fd_lblNewLabel1.top = new FormAttachment(lbl5, 5);
      fd_lblNewLabel1.left = new FormAttachment(10, 0);
      fd_lblNewLabel1.right = new FormAttachment(90, 0);
      fd_lblNewLabel1.height = 1;
      lblNewLabel1.setLayoutData(fd_lblNewLabel1);

      Label lblNewLabel2 = new Label(cmpsStatic, SWT.NONE);
      lblNewLabel2.setText("sss");
      lblNewLabel2.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
      FormData fd_lblNewLabel2 = new FormData();
      fd_lblNewLabel2.bottom = new FormAttachment(100, -60);
      fd_lblNewLabel2.left = new FormAttachment(10, 0);
      fd_lblNewLabel2.right = new FormAttachment(90, 0);
      fd_lblNewLabel2.height = 2;
      lblNewLabel2.setLayoutData(fd_lblNewLabel2);

      scrolledComposite = new ScrolledComposite(cmpsStatic, SWT.V_SCROLL);
      FormData fd_scrolledComposite = new FormData();
      fd_scrolledComposite.left = new FormAttachment(10, 0);
      fd_scrolledComposite.right = new FormAttachment(90, 16);
      fd_scrolledComposite.top = new FormAttachment(lblNewLabel1, 0);
      fd_scrolledComposite.bottom = new FormAttachment(lblNewLabel2, 0);
      scrolledComposite.setLayoutData(fd_scrolledComposite);
      scrolledComposite.setExpandHorizontal(true);
      scrolledComposite.setExpandVertical(true);

      compData = new Composite(scrolledComposite, SWT.NONE);
      compData.setLayout(new FormLayout());

      scrolledComposite.setContent(compData);
      scrolledComposite.setMinSize(new Point(300, 300));

      Label lblSum = new Label(cmpsStatic, SWT.SHADOW_NONE | SWT.RIGHT);
      lblSum.setText("合计：");
      lblSum.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.BOLD));
      FormData fd_lblSum = new FormData();
      fd_lblSum.left = new FormAttachment(10, 0);
      fd_lblSum.right = new FormAttachment(35, 0);
      fd_lblSum.top = new FormAttachment(lblNewLabel2, 5);
      lblSum.setLayoutData(fd_lblSum);

      lbl2sum = new Label(cmpsStatic, SWT.RIGHT);
      lbl2sum.setText(" ");
      lbl2sum.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      FormData fd_lbl2sum = new FormData();
      fd_lbl2sum.top = new FormAttachment(lblNewLabel2, 5);
      fd_lbl2sum.left = new FormAttachment(38, 0);
      fd_lbl2sum.right = new FormAttachment(51, 0);
      lbl2sum.setLayoutData(fd_lbl2sum);

      lbl3sum = new Label(cmpsStatic, SWT.RIGHT);
      lbl3sum.setText(" ");
      lbl3sum.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      FormData fd_lbl3sum = new FormData();
      fd_lbl3sum.top = new FormAttachment(lblNewLabel2, 5);
      fd_lbl3sum.left = new FormAttachment(51, 0);
      fd_lbl3sum.right = new FormAttachment(64, 0);
      lbl3sum.setLayoutData(fd_lbl3sum);

      lbl4sum = new Label(cmpsStatic, SWT.RIGHT);
      lbl4sum.setText(" ");
      lbl4sum.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      FormData fd_lbl4sum = new FormData();
      fd_lbl4sum.top = new FormAttachment(lblNewLabel2, 5);
      fd_lbl4sum.left = new FormAttachment(64, 0);
      fd_lbl4sum.right = new FormAttachment(77, 0);
      lbl4sum.setLayoutData(fd_lbl4sum);

      lbl5sum = new Label(cmpsStatic, SWT.RIGHT);
      lbl5sum.setText(" ");
      lbl5sum.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
      FormData fd_lbl5sum = new FormData();
      fd_lbl5sum.top = new FormAttachment(lblNewLabel2, 5);
      fd_lbl5sum.left = new FormAttachment(77, 0);
      fd_lbl5sum.right = new FormAttachment(90, 0);
      lbl5sum.setLayoutData(fd_lbl5sum);
      shell = parent.getShell();
      labels = new ArrayList<Label>();

   }

   private void query() {
      Date d = dtStartDate.getDate();
      String sDate = d == null ? null : sdf.format(d);
      d = dtEndDate.getDate();
      String eDate = d == null ? null : sdf.format(d);

      List<String[]> rows;
      int idx = cboStation.getSelectionIndex();
      if (idx == 0) rows = querySql(sDate, eDate);
      else {
         List<ResultSetMap> list = (List<ResultSetMap>) cbvStation.getInput();
         ResultSetMap map = list.get(idx);
         rows = querySql(map.get("code").toString(), sDate, eDate);
      }
      delRowAll();
      compData.layout();
      for (int j = 0; j < rows.size(); j++) {
         addRow(j, rows.get(j));
      }
      compData.layout();
      scrolledComposite.setMinSize(new Point(300, rows.size() * 28));
      // lbl5sum.setText("");
   }

   public static final String STATUS_SENT   = "sent";
   public static final String STATUS_UNZIP  = "unzip";
   public static final String STATUS_FINISH = "finish";
   public static final String STATUS_FAIL   = "fail";

   private List<String[]> querySql(String code, String start, String end) {
      NameFilter filter = new NameFilter("cfg");
      int sDate = (start == null || "".equals(start)) ? 20000101 : Integer.parseInt(start);
      int eDate = (end == null || "".equals(end)) ? 20990101 : Integer.parseInt(end);

      String sql = "select o.*,n.ip_addr,n.port_no,n.id from sys_org o left join sys_org_net n on o.org_code=n.org_code ";
      sql += " where substr(o.org_code,0,8)='" + code + "' ";
      sql += " and o.org_type='Z' ";
      sql += " ORDER BY o.ORG_CODE";
      List<String[]> rows = new ArrayList<String[]>();

      List<ResultSetMap> listStations = JdbcDao.query(sql);
      if (listStations == null) {
         Util.alert(shell, "提示", "数据库访问错误。");
         return rows;
      }

      for (ResultSetMap map : listStations) {
         String[] strs = { (map.get("site_code") == null ? "        " : map.get("site_code").toString()) + " " + map.get("org_name").toString(), "0", "0", "0", "0",
               map.get("site_code") == null ? "" : map.get("site_code").toString() };
         rows.add(strs);
      }

      String dasDataDirPath = String.valueOf(parentWindow.getProperties().get("ftpServer.dasDataDir"));
      File compFile = new File(dasDataDirPath + File.separator + code);
      if (!compFile.exists()) return rows;
      File[] statFile = compFile.listFiles();
      for (File sf : statFile) {
         String[] strs = new String[5];
         strs[0] = sf.getName();
         Integer recvCount = 0;
         Integer loadCount = 0;
         Integer sucessCount = 0;
         Integer failCount = 0;

         File[] cfgFiles = sf.listFiles(filter);
         for (File cf : cfgFiles) {
            String cfileName = cf.getName().substring(0, 8);
            int cff = Integer.parseInt(cfileName);
            if (sDate <= cff && cff <= eDate) {
               recvCount++;
               Properties cfg = FileUtil.loadProperties(cf);
               String status = cfg.getProperty("catalog.status");
               if (STATUS_UNZIP.equalsIgnoreCase(status)) {
                  loadCount++;
               }
               else if (STATUS_FINISH.equalsIgnoreCase(status)) {
                  loadCount++;
                  sucessCount++;
               }
               else if (STATUS_FAIL.equalsIgnoreCase(status)) {
                  loadCount++;
                  failCount++;
               }
            }
         }
         strs[1] = recvCount.toString();
         strs[2] = loadCount.toString();
         strs[3] = sucessCount.toString();
         strs[4] = failCount.toString();
         for (String[] row : rows) {
            if (strs[0].equalsIgnoreCase(row[5])) {
               row[1] = strs[1];
               row[2] = strs[2];
               row[3] = strs[3];
               row[4] = strs[4];
               break;
            }
         }
      }
      return rows;
   }

   private List<String[]> querySql(String start, String end) {
      NameFilter filter = new NameFilter("cfg");
      int sDate = (start == null || "".equals(start)) ? 20000101 : Integer.parseInt(start);
      int eDate = (end == null || "".equals(end)) ? 20990101 : Integer.parseInt(end);

      List<String[]> rows = new ArrayList<String[]>();
      List<ResultSetMap> list = parentWindow.getCompanyList();
      for (ResultSetMap map : list) {
         String[] strs = new String[5];
         strs[0] = map.get("name").toString();
         Integer recvCount = 0;
         Integer loadCount = 0;
         Integer sucessCount = 0;
         Integer failCount = 0;
         String dasDataDirPath = String.valueOf(parentWindow.getProperties().get("ftpServer.dasDataDir"));
         File compFile = new File(dasDataDirPath + File.separator + map.get("code").toString());
         if (compFile.exists()) {
            File[] statFile = compFile.listFiles();
            for (File sf : statFile) {
               File[] cfgFiles = sf.listFiles(filter);
               for (File cf : cfgFiles) {
                  String cfileName = cf.getName().length()==18?cf.getName().substring(0, 8):cf.getName().substring(4, 12);
                  int cff = Integer.parseInt(cfileName);
                  if (sDate <= cff && cff <= eDate) {
                     recvCount++;
                     Properties cfg = FileUtil.loadProperties(cf);
                     String status = cfg.getProperty("catalog.status");
                     if (STATUS_UNZIP.equalsIgnoreCase(status)) {
                        loadCount++;
                     }
                     else if (STATUS_FINISH.equalsIgnoreCase(status)) {
                        loadCount++;
                        sucessCount++;
                     }
                     else if (STATUS_FAIL.equalsIgnoreCase(status)) {
                        loadCount++;
                        failCount++;
                     }
                  }
               }
            }
         }
         strs[1] = recvCount.toString();
         strs[2] = loadCount.toString();
         strs[3] = sucessCount.toString();
         strs[4] = failCount.toString();
         rows.add(strs);
      }
      return rows;
   }

   private void delRowAll() {
      if (labels == null) return;
      for (Label label : labels) {
         label.dispose();
      }
      labels.clear();
   }

   private List<Label> labels = null;

   private void addRow(int index, String[] values) {
      for (int i = 0; i < 5; i++) {
         Label label = new Label(compData, SWT.SHADOW_NONE | SWT.RIGHT);
         label.setFont(SWTResourceManager.getFont("微软雅黑", 12, SWT.NORMAL));
         label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

         FormData fd_label = new FormData();
         if (i == 0) {
            fd_label.left = new FormAttachment(0, 0);
            fd_label.right = new FormAttachment(35, 0);
            label.setAlignment(SWT.LEFT);
         }
         else {
            fd_label.left = new FormAttachment(16 * i + 18, 0);
            fd_label.right = new FormAttachment(16 * i + 34, 0);
         }
         fd_label.top = new FormAttachment(0, index * 28 + 5);
         label.setLayoutData(fd_label);
         label.setText(values[i]);
         label.setVisible(true);
         labels.add(label);
      }
   }

   @SuppressWarnings("unchecked")
   public void load() {
      List<ResultSetMap> list = new ArrayList<ResultSetMap>();
      list.addAll(parentWindow.getCompanyList());
      ResultSetMap map = new ResultSetMap();
      map.put("name", "河南销售分公司");
      list.add(0, map);
      cbvStation.setInput(list);
      cboStation.select(0);
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }
}
