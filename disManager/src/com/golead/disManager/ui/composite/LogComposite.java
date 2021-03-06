package com.golead.disManager.ui.composite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import swing2swt.layout.BorderLayout;

import com.golead.disManager.ui.provider.SortByName;
import com.golead.disManager.util.Util;

public class LogComposite extends Composite {
   private Shell      shell;
   private String     filePath = System.getProperty("user.dir") + "/log/";
   private StyledText sttLog;
   private Table      table;

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public LogComposite(Composite parent, int style) {
      super(parent, style);
      shell = parent.getShell();
      setLayout(new FormLayout());
      
      Group group = new Group(this, SWT.NONE);
      FormData fd_group = new FormData();
      fd_group.right = new FormAttachment(100, -2);
      fd_group.bottom = new FormAttachment(100, -2);
      fd_group.top = new FormAttachment(0);
      fd_group.left = new FormAttachment(0,2);
      group.setLayoutData(fd_group);
      group.setLayout(new BorderLayout(0, 0));
      
      Composite composite = new Composite(group, SWT.NONE);
      composite.setLayoutData(BorderLayout.NORTH);
      composite.setLayout(new FormLayout());

      Button btnLogRefreash = new Button(composite, SWT.NONE);
      btnLogRefreash.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            load();
         }
      });
      FormData fd_btnLogRefreash = new FormData();
      fd_btnLogRefreash.left = new FormAttachment(0, 5);
      fd_btnLogRefreash.top = new FormAttachment(0, 5);
      fd_btnLogRefreash.bottom = new FormAttachment(100, -5);
      fd_btnLogRefreash.width = 80;
      btnLogRefreash.setLayoutData(fd_btnLogRefreash);
      btnLogRefreash.setText("    刷新    ");

      Button btnLogDelete = new Button(composite, SWT.NONE);
      btnLogDelete.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int res = Util.confirm(shell, "删除确认", "是否真的要删除这些日志文件？");
            if (res == SWT.YES) deleteLogs();
            load();
         }
      });
      FormData fd_btnLogDelete = new FormData();
      fd_btnLogDelete.bottom = new FormAttachment(100, -5);
      fd_btnLogDelete.left = new FormAttachment(btnLogRefreash, 5);
      fd_btnLogDelete.top = new FormAttachment(0, 5);
      btnLogDelete.setLayoutData(fd_btnLogDelete);
      btnLogDelete.setText("  清除日志  ");

      sttLog = new StyledText(group, SWT.BORDER | SWT.V_SCROLL);
      sttLog.setLayoutData(BorderLayout.CENTER);
      sttLog.setTopIndex(Integer.MAX_VALUE);

      table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseDown(MouseEvent e) {
            TableItem item = table.getItem(new Point(e.x, e.y));
            if (item != null) load(item.getText());
         }
      });

      table.setHeaderVisible(true);
      table.setLayoutData(BorderLayout.WEST);
      table.setLinesVisible(true);

      TableColumn clmnLogName = new TableColumn(table, SWT.NONE);
      clmnLogName.setWidth(200);
      clmnLogName.setText("日志文件");
      

   }

   private void deleteLogs() {
      TableItem[] items = table.getSelection();
      for (TableItem item : items) {
         if ("dasService.log".equalsIgnoreCase(item.getText())) {
            Util.alert(shell, "删除提示", "当前使用的日志文件不能删除！");
            continue;
         }
         File file = new File(filePath + item.getText());
         boolean res = file.delete();
         if (!res) Util.alert(shell, "删除提示", "日志文件删除失败：" + file.getName());
      }
   }

   @SuppressWarnings("unchecked")
   public void load() {
      table.removeAll();
      File dir = new File(filePath);
      List<String> list = new ArrayList<String>();
      for (File file : dir.listFiles()) {
         if (!file.getName().startsWith("disService.log")) continue;
         list.add(file.getName().trim());
      }

      Collections.sort(list, new SortByName());
      String str = list.get(list.size() - 1);
      list.remove(list.size() - 1);
      list.add(0, str);
      for (String name : list) {
         TableItem item = new TableItem(table, SWT.NONE);
         item.setText(new String[] { name });
      }
      load("disService.log");
   }

   public void load(String fileName) {
      FileInputStream in;
      sttLog.setText("");
      StringBuffer sb=new StringBuffer(""); 
      try {
         in = new FileInputStream(filePath + fileName);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
         String tempstr = bufferedReader.readLine();
         while (tempstr != null) {
            sb.append(tempstr).append("\r\n");
            tempstr = bufferedReader.readLine();
         }
         bufferedReader.close();
         sttLog.setText(sb.toString());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }
}
