package com.golead.dasManager.ui.composite;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import swing2swt.layout.BorderLayout;

import com.golead.common.util.FileUtil;
import com.golead.common.util.NameFilter;
import com.golead.dasManager.ui.dialog.CollectDialog;
import com.golead.dasManager.util.Util;

public class FileComposite extends Composite {
   private static Logger             logger     = Logger.getLogger(FileComposite.class);
   private Properties                properties = new Properties();
   private Shell                     shell;
   private Table                     tblFile;
   private String                    filePath   = System.getProperty("user.dir") + "/dat/";
   private SimpleDateFormat          sdf        = new SimpleDateFormat("yyyyMMddHHmmss");
   private SimpleDateFormat          sdf1       = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private String                    createTime;
   private DateComposite             dtCollectStart;
   private DateComposite             dtCollectEnd;
   private DateComposite             dtTransStart;
   private DateComposite             dtTransEnd;
   private TableViewer               tableViewer;
   private List<Map<String, Object>> listData;

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public FileComposite(Composite parent, int style) {
      super(parent, style);
      shell = parent.getShell();
      setLayout(new BorderLayout(0, 0));

      Composite composite = new Composite(this, SWT.NONE);
      composite.setLayoutData(BorderLayout.NORTH);
      composite.setLayout(new FormLayout());

      Label lblCollect = new Label(composite, SWT.NONE);
      lblCollect.setAlignment(SWT.RIGHT);
      FormData fd_lblCollect = new FormData();
      fd_lblCollect.left = new FormAttachment(0, 2);
      fd_lblCollect.top = new FormAttachment(0, 10);
      fd_lblCollect.bottom = new FormAttachment(100, -10);
      fd_lblCollect.width = 100;
      lblCollect.setLayoutData(fd_lblCollect);
      lblCollect.setText("数据采集时间：");

      dtCollectStart = new DateComposite(composite, SWT.BORDER);
      FormData fd_dtCollectStart = new FormData();
      fd_dtCollectStart.top = new FormAttachment(0, 5);
      fd_dtCollectStart.bottom = new FormAttachment(100, -5);
      fd_dtCollectStart.left = new FormAttachment(lblCollect, 5);
      fd_dtCollectStart.width = 100;
      dtCollectStart.setLayoutData(fd_dtCollectStart);
      dtCollectStart.setDate(null);

      Label lblNewLabel = new Label(composite, SWT.CENTER);
      FormData fd_lblNewLabel = new FormData();
      fd_lblNewLabel.top = new FormAttachment(0, 10);
      fd_lblNewLabel.bottom = new FormAttachment(100, -10);
      fd_lblNewLabel.left = new FormAttachment(dtCollectStart, 0);
      fd_lblNewLabel.width = 20;
      lblNewLabel.setLayoutData(fd_lblNewLabel);
      lblNewLabel.setText("至");

      dtCollectEnd = new DateComposite(composite, SWT.BORDER);
      FormData fd_dtCollectEnd = new FormData();
      fd_dtCollectEnd.top = new FormAttachment(0, 5);
      fd_dtCollectEnd.bottom = new FormAttachment(100, -5);
      fd_dtCollectEnd.left = new FormAttachment(lblNewLabel, 0);
      fd_dtCollectEnd.width = 100;
      dtCollectEnd.setLayoutData(fd_dtCollectEnd);
      dtCollectEnd.setDate(null);

      Label lblTrans = new Label(composite, SWT.NONE);
      lblTrans.setAlignment(SWT.RIGHT);
      FormData fd_lblTrans = new FormData();
      fd_lblTrans.top = new FormAttachment(0, 10);
      fd_lblTrans.bottom = new FormAttachment(100, -10);
      fd_lblTrans.left = new FormAttachment(dtCollectEnd, 0);
      fd_lblTrans.width = 100;
      lblTrans.setLayoutData(fd_lblTrans);
      lblTrans.setText("数据传送时间：");

      dtTransStart = new DateComposite(composite, SWT.BORDER);
      FormData fd_dtTransStart = new FormData();
      fd_dtTransStart.left = new FormAttachment(lblTrans, 0);
      fd_dtTransStart.top = new FormAttachment(0, 5);
      fd_dtTransStart.bottom = new FormAttachment(100, -5);
      fd_dtTransStart.width = 100;
      dtTransStart.setLayoutData(fd_dtTransStart);
      dtTransStart.setDate(null);

      Label lblNewLabel_1 = new Label(composite, SWT.CENTER);
      FormData fd_lblNewLabel_1 = new FormData();
      fd_lblNewLabel_1.top = new FormAttachment(0, 10);
      fd_lblNewLabel_1.bottom = new FormAttachment(100, -10);
      fd_lblNewLabel_1.left = new FormAttachment(dtTransStart, 0);
      fd_lblNewLabel_1.width = 20;
      lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
      lblNewLabel_1.setText("至");

      dtTransEnd = new DateComposite(composite, SWT.BORDER);
      FormData fd_dtTransEnd = new FormData();
      fd_dtTransEnd.top = new FormAttachment(0, 5);
      fd_dtTransEnd.bottom = new FormAttachment(100, -5);
      fd_dtTransEnd.left = new FormAttachment(lblNewLabel_1, 0);
      fd_dtTransEnd.width = 100;
      dtTransEnd.setLayoutData(fd_dtTransEnd);
      dtTransEnd.setDate(null);

      Button btnLogSearch = new Button(composite, SWT.NONE);
      FormData fd_btnLogSearch = new FormData();
      fd_btnLogSearch.top = new FormAttachment(0, 5);
      fd_btnLogSearch.bottom = new FormAttachment(100, -5);
      fd_btnLogSearch.left = new FormAttachment(dtTransEnd, 5);
      btnLogSearch.setLayoutData(fd_btnLogSearch);
      btnLogSearch.setText("    查询    ");
      dtTransStart.getData();
      btnLogSearch.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            load();
         }
      });

      Button btnBackup = new Button(composite, SWT.NONE);
      FormData fd_btnBackup = new FormData();
      fd_btnBackup.left = new FormAttachment(btnLogSearch, 5);
      fd_btnBackup.top = new FormAttachment(0, 5);
      fd_btnBackup.bottom = new FormAttachment(100, -5);
      btnBackup.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int res = Util.confirm(shell, "删除确认", "是否真的要删除这些数据文件？");
            if (res == SWT.YES) deleteFiles();
            load();
         }
      });
      btnBackup.setLayoutData(fd_btnBackup);
      btnBackup.setText("  删除文件  ");

      Button btnDataCollect = new Button(composite, SWT.NONE);
      btnDataCollect.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            CollectDialog collectDialog = new CollectDialog(shell, SWT.NONE);
            String result = collectDialog.open();
            if (result == null) return;
            else startCollect(result);
         }
      });
      FormData fd_btnDataCollect = new FormData();
      fd_btnDataCollect.bottom = new FormAttachment(100, -5);
      fd_btnDataCollect.left = new FormAttachment(btnBackup, 5);
      fd_btnDataCollect.top = new FormAttachment(0, 5);
      btnDataCollect.setLayoutData(fd_btnDataCollect);
      btnDataCollect.setText("  采集数据  ");

      Button btnFileReTrans = new Button(composite, SWT.NONE);
      btnFileReTrans.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            send();
         }
      });
      FormData fd_btnFileReTrans = new FormData();
      fd_btnFileReTrans.bottom = new FormAttachment(100, -5);
      fd_btnFileReTrans.left = new FormAttachment(btnDataCollect, 5);
      fd_btnFileReTrans.top = new FormAttachment(0, 5);
      btnFileReTrans.setLayoutData(fd_btnFileReTrans);
      btnFileReTrans.setText("  上传文件  ");

      tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
      tblFile = tableViewer.getTable();
      tblFile.setLayoutData(BorderLayout.CENTER);
      tblFile.setLayoutData(BorderLayout.CENTER);
      tblFile.setHeaderVisible(true);
      tblFile.setLinesVisible(true);

      TableViewerColumn clmnTableView = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn clmnTable = clmnTableView.getColumn();

      clmnTable.setWidth(250);
      clmnTable.setText("数据文件");

      TableViewerColumn clmnCollectTimeView = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn clmnCollectTime = clmnCollectTimeView.getColumn();
      clmnCollectTime.setWidth(150);
      clmnCollectTime.setText("采集时间");

      TableViewerColumn clmnTransTimeView = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn clmnTransTime = clmnTransTimeView.getColumn();
      clmnTransTime.setWidth(150);
      clmnTransTime.setText("传送时间");

      TableViewerColumn clmnStatusView = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn clmnStatus = clmnStatusView.getColumn();
      clmnStatus.setWidth(100);
      clmnStatus.setText("状态");

      TableViewerColumn clmnMemoView = new TableViewerColumn(tableViewer, SWT.NONE);
      TableColumn clmnMemo = clmnMemoView.getColumn();
      clmnMemo.setWidth(500);
      clmnMemo.setText("备注");

      listData = new ArrayList<Map<String, Object>>();
      tableViewer.setContentProvider(new ArrayContentProvider());
      tableViewer.setLabelProvider(new MapLabelProvider());
      tableViewer.setInput(listData);

      Menu popStation = new Menu(tblFile);
      tblFile.setMenu(popStation);

      MenuItem popItemClean = new MenuItem(popStation, SWT.NONE);
      popItemClean.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int res = Util.confirm(shell, "删除确认", "是否真的要删除这些数据文件？");
            if (res == SWT.YES) deleteFiles();
            load();
         }
      });
      popItemClean.setText("删除文件");

      MenuItem popItemTrans = new MenuItem(popStation, SWT.NONE);
      popItemTrans.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            send();
         }
      });
      popItemTrans.setText("上传文件");

   }

   private void startCollect(String result) {
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File oFile = new File(fileName);
      if (oFile.exists()) Util.alert(shell, "提示", "系统繁忙，请稍后再试！");
      String[] paras = result.split(",");
      Properties props = new Properties();
      for (String para : paras) {
         String[] value = para.split(":");
         props.put(value[0], value[1]);
      }
      props.put("operation", "collect");
      FileUtil.saveProperties(oFile, props);
   }

   private boolean isMatch(String checkTime, Date startDate, Date endDate) {
      Date ct;
      try {
         ct = sdf1.parse(checkTime);
      } catch (ParseException e) {
         if (startDate == null && endDate == null) return true;
         else return false;
      }

      if (startDate == null) {
         if (endDate == null) {
            return true;
         }
         else {
            return ct.compareTo(endDate) <= 0 ? true : false;
         }
      }
      else {
         if (endDate == null) {
            return ct.compareTo(startDate) >= 0 ? true : false;
         }
         else {
            return (ct.compareTo(startDate) >= 0 ? true : false) && (ct.compareTo(endDate) <= 0 ? true : false);
         }
      }
   }

   public void sendAll() {
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File oFile = new File(fileName);
      if (oFile.exists()) Util.alert(shell, "提示", "系统繁忙，请稍后再试！");
      Properties props = new Properties();
      TableItem[] items = tblFile.getItems();

      for (int i = 0; i < items.length; i++) {
         TableItem item = items[i];
         String status = item.getText(3);
         if (!"已发送".equals(status)) props.put("file" + i, item.getText(0));
      }
      props.put("operation", "reSend");
      FileUtil.saveProperties(oFile, props);
   }

   public void send() {
      String fileName = System.getProperty("user.dir") + File.separator + "now.properties";
      File oFile = new File(fileName);
      if (oFile.exists()) Util.alert(shell, "提示", "系统繁忙，请稍后再试！");
      Properties props = new Properties();
      TableItem[] items = tblFile.getSelection();

      boolean b = false;
      for (int i = 0; i < items.length; i++) {
         TableItem item = items[i];
         String status = item.getText(3);
         if ("已发送".equals(status)) b = true;
         props.put("file" + i, item.getText(0));
      }
      if (b) {
         int res = Util.confirm(shell, "提示", "部分文件已经上传过，是否要重新上传？");
         if (res == SWT.NO) return;
      }
      props.put("operation", "reSend");
      FileUtil.saveProperties(oFile, props);
   }

   public void resize() {
      int wd = shell.getSize().x;
      TableColumn[] cols = tblFile.getColumns();
      cols[0].setWidth((int) (wd * 0.2));
      cols[1].setWidth((int) (wd * 0.13));
      cols[2].setWidth((int) (wd * 0.13));
      cols[3].setWidth((int) (wd * 0.1));
      cols[4].setWidth((int) (wd * 0.4));
   }

   public void load() {
      File fileDir = new File(filePath);
      FilenameFilter filter = new NameFilter(".cfg");
      File[] list = fileDir.listFiles(filter);

      try {
         for (File file : list) {
            Properties prop = FileUtil.loadProperties(file);

            String fileName = file.getName().replaceAll(".cfg", ".zip");
            createTime = prop.get("catalog.createTime") == null ? "" : sdf1.format(sdf.parse(prop.get("catalog.createTime").toString()));
            if (!isMatch(createTime, dtCollectStart.getDate(), dtCollectEnd.getDate())) continue;
            String sendTime = prop.get("catalog.sendTime") == null ? "" : sdf1.format(sdf.parse(prop.get("catalog.sendTime").toString()));
            if (!isMatch(sendTime, dtTransStart.getDate(), dtTransEnd.getDate())) continue;
            String status = prop.get("catalog.status") == null ? "" : prop.get("catalog.status").toString();
            status = Util.getTransStatusName((status.trim())) == null ? "" : Util.getTransStatusName(status.trim());
            String memo = prop.get("catalog.memo") == null ? "" : prop.get("catalog.memo").toString();

            Map<String, Object> map = null;
            for (Map<String, Object> mapTmp : listData) {
               String fName = mapTmp.get("fileName").toString();
               if (fName.equals(fileName)) {
                  map = mapTmp;
                  break;
               }
            }
            if (map == null) {
               map = new HashMap<String, Object>();
               map.put("fileName", fileName);
               map.put("createTime", createTime);
               map.put("sendTime", sendTime);
               map.put("status", status);
               map.put("memo", memo);
               listData.add(map);
            }
            else {
               map.put("createTime", createTime);
               map.put("sendTime", sendTime);
               map.put("status", status);
               map.put("memo", memo);
            }
         }
      } catch (ParseException pe) {
         logger.error(pe.getMessage());
         pe.printStackTrace();
      }

      List<Map<String, Object>> listD = new ArrayList<Map<String, Object>>();
      for (Map<String, Object> mapTmp : listData) {
         String fName = mapTmp.get("fileName").toString().replaceAll("zip", "cfg");
         boolean res = false;
         for (File file : list) {
            if (fName.equals(file.getName())) {
               res = true;
               break;
            }
         }
         if (!res) listD.add(mapTmp);
      }
      listData.removeAll(listD);
      tableViewer.refresh();
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }

   private void deleteFiles() {
      TableItem[] items = tblFile.getSelection();
      List<String> list = new ArrayList<String>();
      boolean b = false;
      for (int i = 0; i < items.length; i++) {
         TableItem item = items[i];
         String status = item.getText(3);
         if (!"已发送".equals(status)) b = true;
         list.add(item.getText(0));
      }
      if (b) {
         int res = Util.confirm(shell, "提示", "部分文件还没有上传过，是否真的删除？");
         if (res == SWT.NO) return;
      }
      for (String file : list) {
         FileUtil.deleteFile(filePath + file);
         String fdir = file.replaceAll(".zip", "");
         FileUtil.deleteFile(filePath + fdir);
         String fcfg = file.replaceAll("zip", "cfg");
         FileUtil.deleteFile(filePath + fcfg);
      }
   }

   class MapLabelProvider extends LabelProvider implements ITableLabelProvider {
      public String getColumnText(Object element, int columnIndex) {
         if (element instanceof Map) {
            Map<String, Object> p = (Map<String, Object>) element;
            if (columnIndex == 0) {
               return p.get("fileName") == null ? "" : p.get("fileName").toString();
            }
            else if (columnIndex == 1) {
               return p.get("createTime") == null ? "" : p.get("createTime").toString();
            }
            else if (columnIndex == 2) {
               return p.get("sendTime") == null ? "" : p.get("sendTime").toString();
            }
            else if (columnIndex == 3) {
               return p.get("status") == null ? "" : p.get("status").toString();
            }
            else if (columnIndex == 4) { return p.get("memo") == null ? "" : p.get("memo").toString(); }
         }
         return null;
      }

      public Image getColumnImage(Object element, int columnIndex) {
         return null;
      }
   }
}
