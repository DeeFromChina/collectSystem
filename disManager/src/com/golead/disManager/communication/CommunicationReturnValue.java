package com.golead.disManager.communication;

public class CommunicationReturnValue {

   private String command;
   private String status;
   private String returnString;
   private String message;

   public CommunicationReturnValue(String message) {
      String[] r = message.trim().split(",");
      command = r[0];
      status = r[1];
      if (r.length > 2) returnString = r[2];
      this.message=message;
   }

   public String getCommand() {
      return command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getReturnString() {
      return returnString;
   }

   public void setReturnString(String returnString) {
      this.returnString = returnString;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
