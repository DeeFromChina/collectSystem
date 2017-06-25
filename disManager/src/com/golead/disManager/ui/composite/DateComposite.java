package com.golead.disManager.ui.composite;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.golead.disManager.ui.dialog.CalendarDialog;
import com.ibm.icu.util.Calendar;

public class DateComposite extends Composite {

   private Date             date      = null;
   private Composite        parent;
   private Label            lblDate;
   private boolean          allowNull = true;

   private SimpleDateFormat sdf       = new SimpleDateFormat("yyyy-MM-dd");

   /**
    * Create the composite.
    * 
    * @param parent
    * @param style
    */
   public DateComposite(Composite parent, int style) {
      super(parent, SWT.BORDER);
      this.parent = parent;
      setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      setLayout(new FormLayout());
      final Shell shell = parent.getShell();
      CLabel lblSelDate = new CLabel(this, SWT.NONE);
      lblSelDate.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseUp(MouseEvent e) {
            CalendarDialog calendarDialog = new CalendarDialog(shell, SWT.APPLICATION_MODAL);
            date = calendarDialog.open(date, allowNull);
            if (date == null) lblDate.setText("");
            else lblDate.setText(sdf.format(date));
            goListener();
         }
      });
      lblSelDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblSelDate.setImage(SWTResourceManager.getImage(DateComposite.class, "/images/calendar_month16.png"));
      FormData fd_lblSelDate = new FormData();
      fd_lblSelDate.top = new FormAttachment(0, 2);
      fd_lblSelDate.bottom = new FormAttachment(100, -2);
      fd_lblSelDate.right = new FormAttachment(100, 0);
      lblSelDate.setLayoutData(fd_lblSelDate);
      lblSelDate.setText("");

      lblDate = new Label(this, SWT.NONE);
      lblDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
      lblDate.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.NORMAL));
      FormData fd_lblDate = new FormData();
      fd_lblDate.top = new FormAttachment(0, 2);
      fd_lblDate.left = new FormAttachment(0, 2);
      fd_lblDate.bottom = new FormAttachment(100, -2);
      fd_lblDate.right = new FormAttachment(lblSelDate, 0);
      lblDate.setLayoutData(fd_lblDate);
      lblDate.setText("");
   }

   private void goListener() {
      Event event = new Event();
      event.widget = this;
      super.notifyListeners(SWT.MouseUp, event);
   }

   public Date getDate() {
      return date;
   }

   public void allowNull(boolean allowNull) {
      this.allowNull = allowNull;
   }

   public void setDate(Date date) {
      this.date = date;
      lblDate.setText(sdf.format(this.date));
   }

   public Integer getYear() {
      if (date == null) return null;
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      return c.get(Calendar.YEAR);
   }

   public Integer getMonth() {
      if (date == null) return null;
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      return c.get(Calendar.MONTH);
   }

   public Integer getDay() {
      if (date == null) return null;
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      return c.get(Calendar.DAY_OF_MONTH);
   }

   @Override
   protected void checkSubclass() {
      // Disable the check that prevents subclassing of SWT components
   }
}
