package com.golead.disService.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.log4j.Logger;

import com.golead.rmi.DitTransfer;
import com.golead.rmi.OilSaleTransfer;
import com.golead.rmi.impl.DitTransferImpl;
import com.golead.rmi.impl.OilSaleTransferImpl;

public class RmiServer extends Thread {
   private static Logger logger = Logger.getLogger(RmiServer.class);

   public void run() {
      logger.info("rmi启动。");

      try {
         OilSaleTransfer ost = new OilSaleTransferImpl();
         DitTransfer ditt = new DitTransferImpl();
         LocateRegistry.createRegistry(9037);
         Naming.bind("rmi://localhost:9037/oilSaleTransfer", ost);
         logger.info("oilSaleTransfer已绑定。");
         Naming.bind("rmi://localhost:9037/ditTransfer", ditt);
         logger.info("ditTransfer已绑定。");
      } catch (RemoteException e) {
         e.printStackTrace();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (AlreadyBoundException e) {
         e.printStackTrace();
      }

      boolean res = true;
      while (res) {
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      logger.info("rmi关闭。");
   }
}
