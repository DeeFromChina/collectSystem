package com.golead.dasService.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectTillpmntChkTrl extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectTillpmntChkTrl.class);

   public CollectTillpmntChkTrl() {
      this.fileName = "tillpmntChkTrl";
      this.primaryKey = "sernum";
      this.dataName = "POS交易支付信息-支票支付";
      this.tableName = "tillpmnt_chk_trl";
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
      StringBuffer sb = new StringBuffer("select c.* from till t,tillbill b,tillpmnt p,tillpmnt_chk_trl c ");
      sb.append(" where t.pos_batch_id=b.pos_batch_id and t.tillnum=b.tillnum and p.billnum=b.billnum and p.sernum=c.sernum ");
      try {
         sb.append(" and t.posted between '").append(formatDate(sDate)).append("' and '").append(formatDate(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
