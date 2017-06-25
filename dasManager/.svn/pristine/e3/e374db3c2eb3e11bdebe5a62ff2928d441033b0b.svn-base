package com.golead.dasManager.ui.dialog;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.golead.dasManager.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

public class CalendarDialog extends Dialog {

   protected Object      result;

   private Display       display      = null;
   protected Shell       shell;

   private final Label[] lblDays      = new Label[42];

   private Combo         cboYear;
   private Combo         cboMonth;
   private Date          selectedDate = null;

   private Button        btnOk;

   private Button        btnCancel;

   /**
    * Create the dialog.
    * 
    * @param parent
    * @param style
    */
   public CalendarDialog(Shell parent, int style) {
      super(parent, style);
      setText("选择日期");
   }

   /**
    * Open the dialog.
    * 
    * @return the result
    */
   public Date open(Date defaultDate, boolean allowNull) {
      if (defaultDate == null) selectedDate = new Date();
      else selectedDate = defaultDate;
      if (!allowNull) {
         btnCancel.setVisible(false);
         FormData fd = (FormData) btnOk.getLayoutData();
         fd.left = new FormAttachment(50, -40);
         fd.right = new FormAttachment(50, 40);
      }
      createContents();
      shell.open();
      shell.layout();

      Display display = getParent().getDisplay();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch()) {
            display.sleep();
         }
      }
      return selectedDate;
   }

   /**
    * Create contents of the dialog.
    */
   private void createContents() {
      shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
      shell.setSize(260, 320);
      shell.setText("选择日期");
      shell.setLayout(new BorderLayout(0, 0));
      Rectangle r = Display.getDefault().getBounds();
      int shellH = shell.getBounds().height;
      int shellW = shell.getBounds().width;
      shell.setLocation((r.width - shellW) / 2, (r.height - shellH) / 2);

      Composite composite = new Composite(shell, SWT.NONE);
      composite.setLayoutData(BorderLayout.NORTH);
      composite.setLayout(new FormLayout());

      Button btnPrevious = new Button(composite, SWT.NONE);
      btnPrevious.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int cim = cboMonth.getSelectionIndex();
            int ciy = cboYear.getSelectionIndex();
            if (cim == 0) {
               if (ciy > 0) {
                  cboMonth.select(11);
                  cboYear.select(ciy - 1);
               }
            }
            else {
               cboMonth.select(cim - 1);
            }
            fillCalendar();
         }
      });
      FormData fd_btnPrevious = new FormData();
      fd_btnPrevious.left = new FormAttachment(0, 0);
      fd_btnPrevious.right = new FormAttachment(15, -2);
      fd_btnPrevious.top = new FormAttachment(0, 2);
      fd_btnPrevious.bottom = new FormAttachment(100, -2);
      btnPrevious.setLayoutData(fd_btnPrevious);
      btnPrevious.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
      btnPrevious.setText("<");

      Calendar calendar = Calendar.getInstance();
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH);

      cboYear = new Combo(composite, SWT.READ_ONLY);
      cboYear.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            fillCalendar();
         }
      });
      cboYear.setItems(new String[] { "2013年", "2014年", "2015年", "2016年", "2017年", "2018年", "2019年", "2020年", "2021年", "2022年", "2023年", "2024年", "2025年" });
      cboYear.select(year - 2013);
      cboYear.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_cboYear = new FormData();
      fd_cboYear.left = new FormAttachment(15, 2);
      fd_cboYear.right = new FormAttachment(50, -2);
      fd_cboYear.top = new FormAttachment(0, 3);
      fd_cboYear.bottom = new FormAttachment(100, -2);
      cboYear.setLayoutData(fd_cboYear);

      cboMonth = new Combo(composite, SWT.READ_ONLY);
      cboMonth.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            fillCalendar();
         }
      });
      cboMonth.setItems(new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" });
      cboMonth.select(month);
      cboMonth.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_cboMonth = new FormData();
      fd_cboMonth.left = new FormAttachment(50, 2);
      fd_cboMonth.right = new FormAttachment(85, -2);
      fd_cboMonth.top = new FormAttachment(0, 3);
      fd_cboMonth.bottom = new FormAttachment(100, -2);
      cboMonth.setLayoutData(fd_cboMonth);

      Button btnNext = new Button(composite, SWT.NONE);
      btnNext.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            int cim = cboMonth.getSelectionIndex();
            int ciy = cboYear.getSelectionIndex();
            if (cim == 11) {
               if (ciy < cboYear.getItemCount() - 1) {
                  cboMonth.select(0);
                  cboYear.select(ciy + 1);
               }
            }
            else {
               cboMonth.select(cim + 1);
            }
            fillCalendar();
         }
      });
      btnNext.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
      FormData fd_btnNext = new FormData();
      fd_btnNext.left = new FormAttachment(85, 2);
      fd_btnNext.right = new FormAttachment(100, 0);
      fd_btnNext.top = new FormAttachment(0, 2);
      fd_btnNext.bottom = new FormAttachment(100, -2);
      btnNext.setLayoutData(fd_btnNext);
      btnNext.setText(">");

      Composite composite_1 = new Composite(shell, SWT.NONE);
      composite_1.setLayoutData(BorderLayout.CENTER);
      GridLayout gridLayout = new GridLayout(7, true);

      composite_1.setLayout(gridLayout);

      Label lblWeek = new Label(composite_1, SWT.NONE);
      lblWeek.setAlignment(SWT.CENTER);
      GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData.grabExcessHorizontalSpace = true;
      lblWeek.setLayoutData(gridData);
      lblWeek.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek.setText("日");

      Label lblWeek_1 = new Label(composite_1, SWT.NONE);
      lblWeek_1.setAlignment(SWT.CENTER);
      GridData gridData_1 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_1.grabExcessHorizontalSpace = true;
      lblWeek_1.setLayoutData(gridData_1);
      lblWeek_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_1.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_1.setText("一");

      Label lblWeek_2 = new Label(composite_1, SWT.NONE);
      lblWeek_2.setAlignment(SWT.CENTER);
      GridData gridData_2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_2.grabExcessHorizontalSpace = true;
      lblWeek_2.setLayoutData(gridData_2);
      lblWeek_2.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_2.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_2.setText("二");

      Label lblWeek_3 = new Label(composite_1, SWT.NONE);
      lblWeek_3.setAlignment(SWT.CENTER);
      GridData gridData_3 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_3.grabExcessHorizontalSpace = true;
      lblWeek_3.setLayoutData(gridData_3);
      lblWeek_3.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_3.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_3.setText("三");

      Label lblWeek_4 = new Label(composite_1, SWT.NONE);
      lblWeek_4.setAlignment(SWT.CENTER);
      GridData gridData_4 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_4.grabExcessHorizontalSpace = true;
      lblWeek_4.setLayoutData(gridData_4);
      lblWeek_4.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_4.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_4.setText("四");

      Label lblWeek_5 = new Label(composite_1, SWT.NONE);
      lblWeek_5.setAlignment(SWT.CENTER);
      GridData gridData_5 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_5.grabExcessHorizontalSpace = true;
      lblWeek_5.setLayoutData(gridData_5);
      lblWeek_5.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_5.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_5.setText("五");

      Label lblWeek_6 = new Label(composite_1, SWT.NONE);
      lblWeek_6.setAlignment(SWT.CENTER);
      GridData gridData_6 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
      gridData_6.grabExcessHorizontalSpace = true;
      lblWeek_6.setLayoutData(gridData_6);
      lblWeek_6.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblWeek_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
      lblWeek_6.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
      lblWeek_6.setText("六");

      for (int i = 0; i < 42; i++) {
         Label l = new Label(composite_1, SWT.BORDER | SWT.CENTER | SWT.SHADOW_NONE);
         l.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
         l.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
         l.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.NORMAL));
         l.setText(" ");
         GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
         gd.heightHint = 24;
         l.setLayoutData(gd);
         l.setData("index", String.valueOf(i));
         l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
               returnOk();
            }

            @Override
            public void mouseUp(MouseEvent e) {
               Label lbl = (Label) e.getSource();
               selectDay(lbl);
            }
         });
         lblDays[i] = l;
      }
      fillDays(selectedDate);

      Composite compsButton = new Composite(shell, SWT.NONE);
      compsButton.setLayoutData(BorderLayout.SOUTH);
      compsButton.setLayout(new FormLayout());

      btnOk = new Button(compsButton, SWT.NONE);
      FormData fd_btnOk = new FormData();
      fd_btnOk.bottom = new FormAttachment(100, -5);
      fd_btnOk.right = new FormAttachment(50, -5);
      fd_btnOk.top = new FormAttachment(0, 5);
      fd_btnOk.width = 80;
      btnOk.setLayoutData(fd_btnOk);
      btnOk.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            returnOk();
         }
      });
      btnOk.setText("确定(&O)");

      btnCancel = new Button(compsButton, SWT.NONE);
      btnCancel.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e) {
            selectedDate = null;
            shell.close();
         }
      });
      FormData fd_btnCancel = new FormData();
      fd_btnCancel.bottom = new FormAttachment(100, -5);
      fd_btnCancel.top = new FormAttachment(0, 5);
      fd_btnCancel.left = new FormAttachment(50, 5);
      fd_btnCancel.width = 80;
      btnCancel.setLayoutData(fd_btnCancel);
      btnCancel.setText("清空(&C)");
   }

   private void fillCalendar() {
      int cim = cboMonth.getSelectionIndex();
      int ciy = cboYear.getSelectionIndex();
      int year = ciy + 2013;
      int month = cim;
      Calendar fca = Calendar.getInstance();
      fca.set(year, month, 1);
      fillDays(fca.getTime());
   }

   private void fillDays(Date date) {
      Calendar ca = Calendar.getInstance();
      ca.setTime(date);
      int year = ca.get(Calendar.YEAR);
      int month = ca.get(Calendar.MONTH);

      Calendar fca = Calendar.getInstance();
      fca.set(year, month, 1);
      int cd = fca.get(Calendar.DAY_OF_WEEK) - 1;
      fca.add(Calendar.DATE, cd * -1);

      for (int i = 0; i < 42; i++) {
         Label label = lblDays[i];
         label.setText(String.valueOf(fca.get(Calendar.DATE)));
         if (fca.get(Calendar.MONTH) == month) {
            label.setData("date", fca.getTime());
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
            label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
         }
         else {
            label.setData("date", null);
            label.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
            label.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
         }
         fca.add(Calendar.DATE, 1);
      }
      Label labelLast = lblDays[35];
      Date dLast = (Date) labelLast.getData("date");
      boolean sh = dLast == null ? false : true;
      for (int i = 35; i < 42; i++) {
         lblDays[i].setVisible(sh);
      }
   }

   private void selectDay(Label label) {
      Date date = (Date) label.getData("date");
      if (date == null) return;
      for (int i = 0; i < 42; i++) {
         Label ltmp = lblDays[i];
         Date dtmp = (Date) ltmp.getData("date");
         if (dtmp == null) continue;
         if (ltmp.equals(label)) {
            ltmp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
            ltmp.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
            selectedDate = dtmp;
         }
         else {
            ltmp.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
            ltmp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
         }
      }
   }

   private void returnOk() {
      shell.close();
   }
}
