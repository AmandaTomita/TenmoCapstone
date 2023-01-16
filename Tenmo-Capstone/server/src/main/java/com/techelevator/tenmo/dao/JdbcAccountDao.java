package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal findBalance(int userId) {
        BigDecimal balance = null;
        String sql = "SELECT balance FROM account WHERE user_id =?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        if(result.next()){
            balance = result.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int userId) {
        BigDecimal currentBalance = findBalance(userId);
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, currentBalance.add(amountToAdd), userId);
        } catch (DataAccessException e) {
            return BigDecimal.ZERO;
        } finally {
            return findBalance(userId);
        }
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int userId) {
        BigDecimal currentBalance = findBalance(userId);
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, currentBalance.subtract(amountToSubtract).doubleValue(), userId);
        } catch (DataAccessException e) {
            return BigDecimal.ZERO;
        } finally {
            return findBalance(userId);
        }
    }

    private Account mapToRowSet(SqlRowSet accountRowSet) {
        Account account = new Account();
        account.setBalance(accountRowSet.getBigDecimal("balance"));
        account.setId(accountRowSet.getInt("id"));
        account.setUserId(accountRowSet.getInt("userId"));

        return account;
    }

}
