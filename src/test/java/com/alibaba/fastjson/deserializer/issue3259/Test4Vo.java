package com.alibaba.fastjson.deserializer.issue3259;

import java.math.BigDecimal;

public class Test4Vo {
    private BigDecimal c;

    private BigDecimal d;

    public BigDecimal getC() {
        return c;
    }

    public void setC(BigDecimal c) {
        this.c = c;
    }

    public BigDecimal getD() {
        return d;
    }

    public void setD(BigDecimal d) {
        this.d = d;
    }
}