package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {


    BigDecimal findBalance(int userId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int userId);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int userId);
}
