package com.golead.dasService.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectItemdoctrans extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectItemdoctrans.class);

   public CollectItemdoctrans() {
      this.fileName = "itemdoctrans";
      this.primaryKey = "itemdoc_id";
      this.dataName = "itemdoc表";
      this.tableName = "itemdoctrans";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
      Date sDate = (Date)parameters.get("startDate");
      Date eDate = (Date)parameters.get("endDate");
      if(sDate==null){
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")，起始时间为空。");
         return null;
      }
      if(eDate==null){
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")，截止时间为空。");
         return null;
      }
      StringBuffer sb = new StringBuffer("select ITEMDOC_ID,ITEMDOCTYPE_ID,OPERATOR_ID,NOTES,STATUS,LINKEDDOC,DAYBATCH_ID,");
      sb.append("STOCKLOCATION_ID,OPOSITLOCATION_ID,DATE_VAL,DATE_REF,POSTED,LINES,SUBTOTAL_VAL,DISCOUNT_VAL,TAX_VAL,EXTRA_VAL,");
      sb.append("EXPORTSTATUS,ITEMDOC_VER,CURRENCY_ID,CURRENCY_RATE,PMNT_DATE,PMNT_REF,ITEM_QTY,UPC_SUM,PMNT_ID,ISSENDTOHOST,");
      sb.append("EXTREF,SEND_STATUS,LAST_SEND_DATE,EXTREF1,SUPPLIERINVOICEAMOUNT,LINKTODOC,DOC_TYPE_NUMERATOR,ORIGINATOR_REF,");
      sb.append("DOC_GEN,ACKNOWLEDGMENT,MODE as TRAN_MODE from itemdoctrans ");
      try {
         sb.append(" where posted between '").append(formatDate(sDate)).append("' and '").append(formatDate(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
