package com.derbysoft.spitfire.fastjson.dto;

import java.math.BigDecimal;

public class ChargeItemDTO extends AbstractDTO{
    private ChargeUnit unit;
    private ChargeType type;
    private BigDecimal value;
    private String description;

    public void setUnit(ChargeUnit unit) {
        this.unit = unit;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
