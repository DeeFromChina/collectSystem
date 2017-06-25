package com.golead.dasService.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectDaybatch extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectDaybatch.class);
   
   public CollectDaybatch() {
      this.fileName = "daybatch";
      this.primaryKey = "day_batch_id";
      this.dataName = "交易日(日结)表";
      this.tableName = "daybatch";
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
      StringBuffer sb = new StringBuffer("select t.* from daybatch t ");
      try {
         sb.append(" where t.day_batch_date between '").append(dateOnlyFormat(sDate)).append("' and '").append(dateOnlyFormat(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
