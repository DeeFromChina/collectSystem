package com.golead.dasService.collection;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

public interface CollectionInterface {

   public void init(String catalogName, Connection connection, Properties properties);

   public boolean goCollection(Map<String, Object> parameters);

   public void saveProperties(Properties properties);

   public Properties loadProperties();
   
   public String getFileName();

}
