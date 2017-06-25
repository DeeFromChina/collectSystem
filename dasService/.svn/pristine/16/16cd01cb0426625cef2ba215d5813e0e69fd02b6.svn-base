package com.golead.dasService.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectFuelPumpsHose extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectFuelPumpsHose.class);

   public CollectFuelPumpsHose() {
      this.fileName = "fuelPumpsHose";
      this.primaryKey = "pump_id;hose_id";
      this.dataName = "fuelPumpsHose表";
      this.tableName = "fuel_pumps_hose";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
         return "select t.* from fuel_pumps_hose t ";
   }
}
