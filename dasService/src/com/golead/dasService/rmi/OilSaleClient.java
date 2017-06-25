package com.golead.dasService.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.golead.rmi.DitTransfer;
import com.golead.rmi.OilSaleTransfer;

public class OilSaleClient {
   public static void main(String[] args) {
      try {
         OilSaleTransfer ost = (OilSaleTransfer) Naming.lookup("rmi://localhost:9037/oilSaleTransfer");
         DitTransfer dit = (DitTransfer) Naming.lookup("rmi://localhost:9037/ditTransfer");
         String res = ost.transfer("3G05", "");
         System.out.println(res);
          res = dit.transfer("this is a test.");
         System.out.println(res);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (RemoteException e) {
         e.printStackTrace();
      } catch (NotBoundException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
