package com.derbysoft.spitfire.fastjson.dto;

import java.math.BigDecimal;

public class ChargeItemDTO extends AbstractDTO{
    private ChargeUnit unit;
    private ChargeType type;
    private BigDecimal value;
    private String description;

    public ChargeUnit getUnit() {
        return unit;
    }

    public void setUnit(ChargeUnit unit) {
        this.unit = unit;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
