package com.golead.dasService.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectItem extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectItem.class);

   public CollectItem() {
      this.fileName = "item";
      this.primaryKey = "itemid";
      this.dataName = "产品表";
      this.tableName = "item";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
         return "select t.* from item t ";
   }
}
