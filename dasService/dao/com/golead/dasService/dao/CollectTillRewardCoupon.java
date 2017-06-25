package com.golead.dasService.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectTillRewardCoupon extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectTillRewardCoupon.class);

   public CollectTillRewardCoupon() {
      this.fileName = "tillRewardCoupon";
      this.primaryKey = "sernum";
      this.dataName = "POS交易客户优惠表";
      this.tableName = "till_reward_coupon";
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
      StringBuffer sb = new StringBuffer("select b.* from till t,till_reward_coupon b ");
      sb.append(" where t.pos_batch_id=b.pos_batch_id and t.tillnum=b.tillnum ");
      try {
         sb.append(" and t.posted between '").append(formatDate(sDate)).append("' and '").append(formatDate(eDate)).append("'");
         return sb.toString();
      } catch (ParseException e) {
         logger.error("数据采集参数错误：" + dataName + "(" + fileName + ")"+sDate.toString()+";"+eDate.toString());
         return null;
      }
   }
}
