package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfer(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE from_username = ? OR to_username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username, username);

        while(result.next()) {
            Transfer transfer = mapToRowSet(result);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if(result.next()){
            transfer = mapToRowSet(result);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer createTransfer) {
        String sql = "INSERT INTO transfer (from_username, to_username, transfer_amount, date_and_time, status) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        LocalDateTime currentDateTime = LocalDateTime.now();
        String status = "Approved";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                createTransfer.getFromUsername(),
                createTransfer.getToUsername(),
                createTransfer.getTransferAmount(),
                currentDateTime,
                status);
        return getTransfer(transferId);

    }

    @Override
    public Transfer sendTransfer(Transfer newTransfer) {
        String sql = "INSERT INTO transfer (from_username, to_username, transfer_amount, date_and_time, status) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        LocalDateTime currentDateTime = LocalDateTime.now();
        String status = "Approved";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromUsername(),
                newTransfer.getToUsername(),
                newTransfer.getTransferAmount(),
                currentDateTime,
                status);
        newTransfer = getTransfer(transferId);
        String sqlUpdate = "UPDATE account SET balance = balance + ? WHERE user_id IN (SELECT user_id FROM tenmo_user WHERE username = ?)";
        jdbcTemplate.update(sqlUpdate, newTransfer.getTransferAmount(), newTransfer.getFromUsername());
        String sqlUpdateSubtract = "UPDATE account SET balance = balance - ? WHERE user_id IN (SELECT user_id FROM tenmo_user WHERE username = ?)";
        jdbcTemplate.update(sqlUpdateSubtract, newTransfer.getTransferAmount(), newTransfer.getFromUsername());
        return newTransfer;
    }

    private Transfer mapToRowSet(SqlRowSet transferRowSet) {
        Transfer transfer = new Transfer();
        transfer.setId(transferRowSet.getInt("transfer_id"));
        transfer.setDate(transferRowSet.getDate("date_and_time").toLocalDate());
        transfer.setFromUsername(transferRowSet.getString("from_username"));
        transfer.setToUsername(transferRowSet.getString("to_username"));
        transfer.setTransferAmount(transferRowSet.getBigDecimal("transfer_amount"));
        transfer.setStatus(transferRowSet.getString("status"));
        return transfer;
    }
}
//    @Override
//    public Transfer getStatus(Transfer transfer) {
//        String sql = "SELECT * FROM transfer WHERE id = ?";
//        Transfer transfer2 = null;
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer);
//        if(result.next()) {
//            transfer = mapToRowSet(result);
//        }
//        return transfer;
//    }
//      @Override
//    public List<Transfer> getPendingStatus(String status) {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT * FROM transfer WHERE status iLIKE ?";
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, status);
//
//        while(result.next()) {
//            Transfer transfer = mapToRowSet(result);
//            transfers.add(transfer);
//        }
//        return transfers;
//    }