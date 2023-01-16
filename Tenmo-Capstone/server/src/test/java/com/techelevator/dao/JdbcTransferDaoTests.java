package com.techelevator.dao;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, LocalDate.now(), "bob", "user", BigDecimal.valueOf(100.00).setScale(2), "Approved");


    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getAllTransferTest(){
        int expectedNumOfTransfers = 1;
        List<Transfer> newTransfer = sut.getAllTransfer("bob");
        Assert.assertEquals(expectedNumOfTransfers, newTransfer.size());
    }
    @Test
    public void getAllTransferTestNull(){
        int expectedNumOfTransfers2 = 0;
        List<Transfer> newTransfer2 = sut.getAllTransfer("");
        Assert.assertEquals(expectedNumOfTransfers2, newTransfer2.size());
    }

    @Test
    public void getTransferTest(){
        Transfer transferById = sut.getTransfer(3001);
        assertTransferMatch(TRANSFER_1, transferById);
    }

    @Test
    public void getTransferTestNull(){
        Transfer transferById = sut.getTransfer(3100);
        Assert.assertNull(transferById);
    }

    @Test
    public void createTransferTest(){
        Transfer createTransfer = new Transfer(3003, LocalDate.now(), "bob", "user", BigDecimal.valueOf(100.00).setScale(2), "Approved");
        Transfer actualTransfer = sut.createTransfer(createTransfer);
        Integer newId = createTransfer.getId();
        createTransfer.setId(newId);
        assertTransferMatch(createTransfer, actualTransfer);
    }

    @Test
    public void sendTransferTest(){
        Transfer sendTransfer = new Transfer(3002, LocalDate.now(), "bob", "user", BigDecimal.valueOf(100.00).setScale(2), "Approved");
        Transfer actualTransfer = sut.createTransfer(sendTransfer);
        Integer newId = sendTransfer.getId();
        sendTransfer.setId(newId);

        assertTransferMatch(sendTransfer, actualTransfer);

    }

    private void assertTransferMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getDate(), actual.getDate());
        Assert.assertEquals(expected.getFromUsername(), actual.getFromUsername());
        Assert.assertEquals(expected.getToUsername(), actual.getToUsername());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());
    }
}
