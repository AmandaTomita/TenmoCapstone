package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Transfer {
    @JsonProperty("transfer_id")
    private int id;
    @JsonProperty("date_and_time")
    private LocalDate date;
    @JsonProperty("from_username")
    private String fromUsername;
    @JsonProperty("to_username")
    private String toUsername;
    @JsonProperty("transfer_amount")
    private BigDecimal transferAmount;
    private String status;

    public Transfer() {
    }

    public Transfer(int id, LocalDate date, String fromUsername, String toUsername, BigDecimal transferAmount, String status) {
        this.id = id;
        this.date = date;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.transferAmount = transferAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", date=" + date +
                ", fromUsername='" + fromUsername + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", transferAmount=" + transferAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
