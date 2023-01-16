package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> getAllTransfer(String username);
    public Transfer getTransfer(int id);
    public Transfer createTransfer (Transfer createTransfer);
    public Transfer sendTransfer(Transfer newTransfer);

}

//    public List<Transfer> getPendingStatus(String status);
//    public Transfer getStatus(int id);