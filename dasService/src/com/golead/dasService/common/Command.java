package com.golead.dasService.common;

import java.util.HashMap;
import java.util.Map;

public class Command {

   public static String        COMMAND_CONNECT        = "connect";
   public static String        COMMAND_DISCONNECT     = "disConnect";
   public static String        COMMAND_COLLECT        = "collect";
   public static String        COMMAND_AUTOCOLLECT    = "autoCollect";
   public static String        COMMAND_SEND           = "send";
   public static String        COMMAND_COLLECTANDSEND = "collectAndSend";
   public static String        COMMAND_LOG            = "log";
   public static String        COMMAND_FILELIST       = "fileList";
   public static String        COMMAND_MESSAGE        = "message";
   public static String        COMMAND_TRANSQUERY     = "transQuery";

   private String              name;
   private Map<String, String> parameters;
   private String              commandString;

   public Command(String commandString) {
      parameters = new HashMap<String, String>();
      this.commandString = commandString;
      genarateCommand();
   }

   private void genarateCommand() {
      String[] paras = commandString.split(",");
      if (paras == null) return;
      String[] comm = paras[0].split(":");
      name = comm[1];
      if (paras.length == 1) return;
      for (int i = 1; i < paras.length; i++) {
         String[] s = paras[i].split(":");
         parameters.put(s[0], s[1]);
      }
   }

   public String getCommandString() {
      return commandString;
   }

   public void setCommandString(String commandString) {
      this.commandString = commandString;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Map<String, String> getParameters() {
      return parameters;
   }

   public void setParameters(Map<String, String> parameters) {
      this.parameters = parameters;
   }
}
