
package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {
    private UserDao dao;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public UserController(AccountDao accountDao, UserDao userDao, TransferDao transferDao) {
        this.dao = userDao;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<String> findAllUsers() {
        List<String> username = new ArrayList<>();
        List<User> users = dao.findAll();
        for (User user : users) {
            username.add(user.getUsername());
        }
        return username;
    }

}
