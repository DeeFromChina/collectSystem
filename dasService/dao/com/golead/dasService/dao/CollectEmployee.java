package com.golead.dasService.dao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.golead.dasService.collection.CollectionBase;
import com.golead.dasService.collection.CollectionInterface;

public class CollectEmployee extends CollectionBase implements CollectionInterface {

   private static Logger logger = Logger.getLogger(CollectEmployee.class);

   public CollectEmployee() {
      this.fileName = "employee";
      this.primaryKey = "empno";
      this.dataName = "用户表";
      this.tableName = "employee";
   }

   @Override
   public String prepareSql(Map<String, Object> parameters) {
         return "select t.* from employee t ";
   }
}
