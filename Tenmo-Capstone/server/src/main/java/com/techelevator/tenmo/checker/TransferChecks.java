package com.techelevator.tenmo.checker;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.text.DecimalFormat;


public class TransferChecks {

    TransferDao transferDao;
    AccountDao accountDao;
    UserDao userDao;
    private TransferChecks checker;
    private Transfer transfer;
    private String fromUser = transfer.getFromUsername();
    private String toUser = transfer.getToUsername();
    private BigDecimal transferAmount = transfer.getTransferAmount();

    public TransferChecks(TransferChecks checker) {
        this.checker = checker;
    }

    private void addTransferRecord(Transfer transfer) {
        transferDao.createTransfer(transfer);
    }

    public TransferChecks(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    public void performTransfer(Transfer transfer) {
        //1. check transfer is valid, e.g. insufficient funds etc
        if (isTransferValid(transfer)) {//you can use boolean or custom exceptions
            //2. deduct amount from sender
            deductMoney(fromUser, transferAmount);
            //3. add amount to receiver
            addMoney(toUser,transferAmount);
            //4. add transaction record to transfer table to db
            addTransferRecord(transfer);
        }
    }

    private boolean isTransferValid(Transfer transfer) {

        if(fromUser.equalsIgnoreCase(toUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cant send money to yourself");
        }
        if(accountDao.findBalance(userDao.findIdByUsername(transfer.getFromUsername())).compareTo(transferAmount) == -1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You have no money left");
        }
        if(toUser.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter an Username");
        }
        if (userDao.findByUsername(transfer.getToUsername()).equals(null)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found");
        }
        if(fromUser.equals(null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter an Username");
        }
        if(transferAmount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter an Amount");
        }
        if (transferAmount.compareTo(new BigDecimal("0.01")) == -1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must enter an Amount greater than 0.00");
        }
        return true;
    }

    private void deductMoney(String sender, BigDecimal amount) {
        // get current balance
        int userId = userDao.findIdByUsername(sender);
        // subtract amount to current money
        //update balance
        accountDao.subtractFromBalance(amount, userId);
    }
    private void addMoney(String receiver, BigDecimal amount) {
        int userId = userDao.findIdByUsername(receiver);
        // get current balance
        // add amount to current money
        //update balance
        accountDao.addToBalance(amount, userId);
    }
}

