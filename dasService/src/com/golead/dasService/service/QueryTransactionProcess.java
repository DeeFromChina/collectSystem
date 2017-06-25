package com.golead.dasService.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

public class QueryTransactionProcess {

   private static Logger logger = Logger.getLogger(QueryTransactionProcess.class);

   private Properties    properties;

   public QueryTransactionProcess() {
      getProperties();
   }

   public boolean getProperties() {
      try {
         if (properties == null) properties = new Properties();
         else properties.clear();
         Properties prop = new Properties();
         InputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + "config.properties");
         BufferedReader bf = new BufferedReader(new InputStreamReader(in));
         prop.load(bf);
         properties.putAll(prop);
         return true;
      } catch (IOException e) {
         return false;
      }
   }

   public String query(String id) {
      if (id == null || "".equals(id)) return null;
      try {
         Class.forName("interbase.interclient.Driver");
      } catch (ClassNotFoundException e) {
         return null;
      }

      Connection connection = openConnection();
      if (connection == null) return null;

      String res = generateData(connection, id);

      closeConnection(connection);

      return res;
   }

   private String generateData(Connection connection, String id) {
      StringBuffer sb = new StringBuffer("");
      Statement stmt = null;

      try {
         stmt = connection.createStatement();
      } catch (SQLException e) {
         return null;
      }

      String sql = "select store_name from store";
      try {
         ResultSet rs = stmt.executeQuery(sql);
         if (rs != null) {
            if (rs.next()) {
               byte[] bytes = rs.getBytes("store_name");
               String storeName;
               try {
                  storeName = new String(bytes, "GBK");
               } catch (UnsupportedEncodingException e) {
                  storeName = "";
               }
               if (!rs.wasNull()) sb.append("siteName:\"" + storeName.trim() + "\",");
            }
            try {
               rs.close();
               rs = null;
            } catch (SQLException re) {
            }
         }
         else {
            return sb.toString();
         }

         // transactionDate:"",transactionTime:"",siteName:"",itemCount:"",totalAmount:"",
         // invoiceAmount:"",discountAmount:""

         SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
         SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");

         sql = "select e.fullname,b.ssum,t.item_sold,t.posted,count(*) as cnt";
         sql += " from tillbill b,till t,cashierbatch c,employee e,tillitem i ";
         sql += " where c.cashier_batch_id=t.cashier_batch_id and c.cashier_id=e.empno and b.tillnum=" + id + " and t.tillnum=" + id + " and i.tillnum=t.tillnum";
         sql += " group by e.fullname,b.ssum,t.item_sold,t.posted";
         ResultSet rs1 = stmt.executeQuery(sql);
         if (rs1 != null) {
            if (rs1.next()) {
               Date posted = rs1.getDate("posted");
               if (!rs1.wasNull()) {
                  sb.append("transactionDate:\"" + sdf1.format(posted) + "\",");
                  sb.append("transactionTime:\"" + sdf2.format(posted) + "\",");
               }
               else {
               }
               byte[] bytes = rs1.getBytes("fullname");
               String fullname;
               try {
                  fullname = new String(bytes, "GBK");
               } catch (UnsupportedEncodingException e) {
                  fullname = "";
               }
               if (!rs1.wasNull()) sb.append("cashierName:\"" + fullname.trim() + "\",");

               String ssum = rs1.getString("ssum");
               if (!rs1.wasNull()) sb.append("totalAmount:\"" + ssum + "\",");
               String cnt = rs1.getString("cnt");
               if (!rs1.wasNull()) sb.append("itemCount:\"" + cnt + "\",");
            }
            try {
               rs1.close();
               rs1 = null;
            } catch (SQLException re) {
            }
         }
         else {
            return sb.toString();
         }

         sql = "select total_savings from tillpromo t where t.tillnum=" + id;
         ResultSet rs0 = stmt.executeQuery(sql);
         if (rs0 != null && rs0.next()) {
            String savings = rs0.getString("total_savings");
            if (!rs0.wasNull()) {
               sb.append("discountAmount:\"" + savings + "\",");
            }
            else{
               sb.append("discountAmount:\"0\",");
            }
            try {
               rs0.close();
               rs0 = null;
            } catch (SQLException re) {
            }
         }
         else {
            sb.append("discountAmount:\"0\",");
         }

         sql = "select stdprice,price,qty,total,discount,weight,t.barcode,i.itemname,i.fulldescription from tillitem t,item i ";
         sql += " where t.barcode=i.barcode and t.tillnum=" + id + " order by sernum";
         ResultSet rs2 = stmt.executeQuery(sql);
         if (rs2 != null) {
            sb.append("items:[");
            while (rs2.next()) {
               sb.append("{");
               byte[] bytes = rs2.getBytes("itemname");
               String itemname;
               try {
                  itemname = new String(bytes, "GBK");
               } catch (UnsupportedEncodingException e) {
                  itemname = "";
               }
               if (!rs2.wasNull()) sb.append("itemName:\"" + itemname + "\",");
               String barcode = rs2.getString("barcode");
               if (!rs2.wasNull()) sb.append("itemCode:\"" + barcode.trim() + "\",");
               String stdprice = rs2.getString("price");
               if (!rs2.wasNull()) sb.append("unitPrice:\"" + stdprice + "\",");
               if (barcode != null && barcode.startsWith("3")) {
                  String weight = rs2.getString("weight");
                  if (!rs2.wasNull()) sb.append("itemQuantity:\"" + weight + "\",");
               }
               else {
                  String qty = rs2.getString("qty");
                  if (!rs2.wasNull()) sb.append("itemQuantity:\"" + qty + "\",");
               }
               String total = rs2.getString("total");
               if (!rs2.wasNull()) sb.append("itemAmount:\"" + total + "\"");
               sb.append("},");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("]");
            try {
               rs2.close();
               rs2 = null;
            } catch (SQLException re) {
            }
         }
      } catch (SQLException e) {
         if (stmt != null) {
            try {
               stmt.close();
               stmt = null;
            } catch (SQLException se) {
            }
         }
         return null;
      }
      return sb.toString();
   }

   private Connection openConnection() {
      if (properties == null) return null;
      String hostname = properties.getProperty("server.severName");
      String port = properties.getProperty("server.port");
      String databaseName = properties.getProperty("server.dbName");
      String userName = properties.getProperty("server.userName");
      String password = properties.getProperty("server.password");
      if (hostname == null || port == null || databaseName == null || userName == null || password == null) return null;

      String url = "jdbc:interbase://" + hostname + ":" + port + "/" + databaseName;
      try {
         return DriverManager.getConnection(url, userName, password);
      } catch (SQLException e) {
         return null;
      }
   }

   private void closeConnection(Connection connection) {
      try {
         if (connection != null && !connection.isClosed()) {
            connection.close();
         }
      } catch (SQLException e) {
      }
   }
}
