package com.golead.dasService.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectFuelGrade extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectFuelGrade.class);

   public CollectFuelGrade() {
      this.fileName = "fuelGrade";
      this.primaryKey = "grade";
      this.dataName = "fuelGrade表";
      this.tableName = "fuelGrade";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
         return "select t.* from fuelGrade t ";
   }
}
