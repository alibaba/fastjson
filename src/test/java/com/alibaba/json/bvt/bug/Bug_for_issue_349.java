package com.alibaba.json.bvt.bug;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_349 extends TestCase {
    public void test_for_issue() throws Exception {
        Money money = new Money();
        money.currency = Currency.getInstance("CNY");
        money.amount = new BigDecimal("10.03");

        String json = JSON.toJSONString(money);

        Money moneyBack = JSON.parseObject(json, Money.class);
        Assert.assertEquals(money.currency, moneyBack.currency);
        Assert.assertEquals(money.amount, moneyBack.amount);

        JSONObject jsonObject = JSON.parseObject(json);
        Money moneyCast = JSON.toJavaObject(jsonObject, Money.class);
        Assert.assertEquals(money.currency, moneyCast.currency);
        Assert.assertEquals(money.amount, moneyCast.amount);
    }
    
    public static class Money {
        public Currency currency;
        public BigDecimal amount;

        @Override
        public String toString() {
            return "Money{currency=" + currency + ", amount=" + amount + '}';
        }
    }
}
