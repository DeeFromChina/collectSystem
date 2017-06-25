package com.golead.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import com.golead.rmi.OilSaleTransfer;

public class OilSaleTransferImpl extends UnicastRemoteObject implements OilSaleTransfer {
   private static final long serialVersionUID = -4716817233718444223L;

   public OilSaleTransferImpl() throws RemoteException {
      super();
   }

   @Override
   public String transfer(String stationCode, String data) throws RemoteException {
      System.out.println(stationCode);
      return "OilSale";
   }

}
