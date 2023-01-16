package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;

public class Account {
    @JsonProperty ("account_id")
    private int id;
    @JsonProperty ("user_id")
    private int userId;
    private BigDecimal balance;


   public Account() {
   }

  public Account(int id, int userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
