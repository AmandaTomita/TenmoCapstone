package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.checker.TransferChecks;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao dao;
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferChecks checks;

    public TransferController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.dao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.checks = new TransferChecks(transferDao, accountDao, userDao);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfer(Principal principal) {

        String username = principal.getName();

        return dao.getAllTransfer(username);
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id,  Principal principal) {
        String username = principal.getName();
        Transfer transfer = dao.getTransfer(id);
        if (!username.equals(transfer.getToUsername())){
            if (!username.equals(transfer.getFromUsername())){
                throw new SecurityException("Unable to access!");
            }
        }
        if (username.equals("")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return transfer;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/transfer")
    public void sendTransfer(@RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        if (!transfer.getFromUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't access"); }
        checks.performTransfer(transfer);
    }




//    @RequestMapping(path = "/transfer/{status}", method = RequestMethod.GET)
//    public List<Transfer> getPendingStatus(@RequestParam(defaultValue = "") String status, Principal principal) {
//        List<Transfer> transferList = null;
//        String username = principal.getName();
//        int userId = userDao.findIdByUsername(username);
//        return dao.getPendingStatus(status);
//    }

}
//    @RequestMapping(path = "/transfer/{status}", method = RequestMethod.GET)
//    public Transfer getStatus(Principal principal, @PathVariable ) {
//        Transfer transfer = dao.getTransfer(id);
//        if (transfer == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        } else {
//            return transfer;
//        }
//    }
