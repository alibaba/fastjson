package com.alibaba.json.bvt.support.moneta;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.math.BigDecimal;

/**
 * @Author ：Nanqi
 * @Date ：Created in 01:31 2020/7/4
 */
public class MoneyNumberTest extends TestCase {
    public void test_for_issue() throws Exception {
        // Integer
        Money money = Money.of(5000, Monetary.getCurrency("EUR"));
        String moneyJSON = JSON.toJSONString(money);
        Money moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(5000, moneyBack.getNumber().intValue());

        // Long
        money = Money.of(1000L, Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(1000, moneyBack.getNumber().longValue());

        // Byte
        money = Money.of(0x4a, Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(74, moneyBack.getNumber().intValue());

        // double
        money = Money.of(new Double(1.12), Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(1.12d, moneyBack.getNumber().doubleValue());

        // float
        money = Money.of(new Float("2.01"), Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(2.01f, moneyBack.getNumber().floatValue());

        // short
        money = Money.of(new Short("2"), Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals(2, moneyBack.getNumber().shortValue());

        // BigInteger
        money = Money.of(new BigDecimal("999999999999999999999"), Monetary.getCurrency("EUR"));
        moneyJSON = JSON.toJSONString(money);
        moneyBack = JSON.parseObject(moneyJSON, Money.class);
        assertEquals("999999999999999999999", moneyBack.getNumber().toString());
    }
}
