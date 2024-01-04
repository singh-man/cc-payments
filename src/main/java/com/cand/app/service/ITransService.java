package com.cand.app.service;

import java.math.BigDecimal;

public interface ITransService {

    /**
     * Returns the balance amount
     */
    BigDecimal addAmount(long accountNo, BigDecimal amount);

    /**
     * Returns the balance amount
     */
    BigDecimal deductAmount(long accountNo, BigDecimal amount);

    BigDecimal balance(long accountNo);

}
