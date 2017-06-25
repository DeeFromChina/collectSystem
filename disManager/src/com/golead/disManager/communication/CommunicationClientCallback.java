package com.golead.disManager.communication;

public class CommunicationClientCallback {

   public static String COMMAND_CONNECT      = "connect";
   public static String COMMAND_DISCONNECT   = "disConnect";
   public static String COMMAND_COLLECT      = "collect";
   public static String COMMAND_SEND         = "send";
   public static String COMMAND_COLLECT_SEND = "collectAndSend";
   public static String COMMAND_LOG          = "log";
   public static String COMMAND_FILE_LIST    = "fileList";
   public static String COMMAND_MESSAGE      = "message";

   public void command(String source, String message) {
   }
}
