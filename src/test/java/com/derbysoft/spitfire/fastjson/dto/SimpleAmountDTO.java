package com.derbysoft.spitfire.fastjson.dto;

import java.math.BigDecimal;

public class SimpleAmountDTO extends AbstractDTO{
    private Currency currency;
    private BigDecimal amount;

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
