package com.golead.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface OilSaleTransfer extends Remote {
    public String transfer(String stationCode,String data) throws RemoteException;
}
