package com.golead.dasService.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectPosbatch extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectPosbatch.class);
  
   public CollectPosbatch() {
      this.fileName = "posbatch";
      this.primaryKey = "pos_batch_id";
      this.dataName = "交易班次(班结)表";
      this.tableName = "posbatch";
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
      StringBuffer sb = new StringBuffer("select p.* from posbatch p,daybatch t where p.day_batch_id=t.day_batch_id");
      try {
         sb.append(" and t.day_batch_date between '").append(dateOnlyFormat(sDate)).append("' and '").append(dateOnlyFormat(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
