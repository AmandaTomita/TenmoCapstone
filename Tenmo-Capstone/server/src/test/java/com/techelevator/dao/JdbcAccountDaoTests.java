package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.0));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(999.0));

    private JdbcAccountDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void findBalanceTest(){
        BigDecimal expectedBalance = BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actualBalance = sut.findBalance(1001);
        Assert.assertEquals(expectedBalance, actualBalance);
    }


    @Test
    public void findBalanceTestNull(){
        BigDecimal expectedBalance = BigDecimal.valueOf(1000.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actualBalance = sut.findBalance(1100);
        Assert.assertNull(actualBalance);
    }

    @Test
    public void findBalanceTestOnUser(){
        BigDecimal expectedBalance = BigDecimal.valueOf(950.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal actualBalance = sut.findBalance(1002);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void addToBalanceTest() {
        BigDecimal expectedBalance = BigDecimal.valueOf(1050.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToAdd = new BigDecimal("100.0");
        BigDecimal actualBalance = sut.findBalance(1002).add(amountToAdd);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void addToBalanceTestSmall() {
        BigDecimal expectedBalance = BigDecimal.valueOf(1000.01).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToAdd = new BigDecimal("0.01");
        BigDecimal actualBalance = sut.findBalance(1001).add(amountToAdd);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void addToBalanceTestLarge() {
        BigDecimal expectedBalance = BigDecimal.valueOf(1950.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToAdd = new BigDecimal("1000.00");
        BigDecimal actualBalance = sut.findBalance(1002).add(amountToAdd);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void subtractToBalanceTest() {
        BigDecimal expectedBalance = BigDecimal.valueOf(900.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToSubtract = new BigDecimal("100.0");
        BigDecimal actualBalance = sut.findBalance(1001).subtract(amountToSubtract);
        Assert.assertEquals(expectedBalance, actualBalance);
    }
    @Test
    public void subtractToBalanceTestSmall() {
        BigDecimal expectedBalance = BigDecimal.valueOf(990.0).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToSubtract = new BigDecimal("10.0");
        BigDecimal actualBalance = sut.findBalance(1001).subtract(amountToSubtract);
        Assert.assertEquals(expectedBalance, actualBalance);
    }
    @Test
    public void subtractToBalanceTestLarge() {
        BigDecimal expectedBalance = BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountToSubtract = new BigDecimal("950.0");
        BigDecimal actualBalance = sut.findBalance(1002).subtract(amountToSubtract);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    private void assertAccountMatch (Account expected, Account actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }

}
