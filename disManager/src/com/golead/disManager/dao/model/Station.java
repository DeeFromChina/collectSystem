package com.golead.disManager.dao.model;

public class Station {
   
   private int id;
   private String code;
   private String name;
   private String shortName;
   private String ip;
   private String port;

   public String getShortName() {
      return shortName;
   }
   public void setShortName(String shortName) {
      this.shortName = shortName;
   }
   public String getIp() {
      return ip;
   }
   public void setIp(String ip) {
      this.ip = ip;
   }
   public String getPort() {
      return port;
   }
   public void setPort(String port) {
      this.port = port;
   }
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
   
   public String getCode() {
      return code;
   }
   public void setCode(String code) {
      this.code = code;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
}
