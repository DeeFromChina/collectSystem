package com.golead.dasService.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectFuelTanks extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectFuelTanks.class);

   public CollectFuelTanks() {
      this.fileName = "fuelTanks";
      this.primaryKey = "tank_id";
      this.dataName = "油罐表";
      this.tableName = "fuel_tanks";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
         return "select t.* from fuel_tanks t ";
   }
}
