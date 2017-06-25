package com.golead.dasService.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectFuelTankReading extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectFuelTankReading.class);

   public CollectFuelTankReading() {
      this.fileName = "fueltankreading";
      this.primaryKey = "shift_control_id;tank_id";
      this.dataName = "加油站油品帐数据表";
      this.tableName = "fuel_tank_reading";
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
      StringBuffer sb = new StringBuffer("select r.* from daybatch t,fuel_shift_control b,fuel_tank_reading r ");
      sb.append(" where t.day_batch_id=b.day_batch_id and b.shift_control_id=r.shift_control_id ");
      try {
         sb.append(" and t.day_batch_date between '").append(formatDate(sDate)).append("' and '").append(formatDate(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
