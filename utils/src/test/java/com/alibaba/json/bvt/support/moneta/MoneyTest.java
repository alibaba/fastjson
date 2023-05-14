package com.alibaba.json.bvt.support.moneta;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.math.BigDecimal;

public class MoneyTest extends TestCase {
    public void test_for_issue() throws Exception {
        Money money = Money.of(new BigDecimal("321.789"), Monetary.getCurrency("EUR"));

        String json = JSON.toJSONString(money);
        assertEquals("{\"numberStripped\":321.789,\"currency\":\"EUR\"}", json);
        Money money2 = JSON.parseObject(json, Money.class);
        assertEquals(Monetary.getCurrency("EUR"), money2.getCurrency());
        assertEquals(new BigDecimal("321.789"), money2.getNumber().numberValue(BigDecimal.class));
    }

    public void test_compatible() throws Exception {
        String json = "{\"context\":{\"amountType\":\"org.javamoney.moneta.Money\",\"empty\":false,\"fixedScale\":false,\"maxScale\":-1,\"precision\":256},\"currency\":{\"context\":{\"empty\":false,\"providerName\":\"java.util.Currency\"},\"currencyCode\":\"EUR\",\"defaultFractionDigits\":2,\"numericCode\":978},\"factory\":{\"amountType\":\"org.javamoney.moneta.Money\",\"defaultMonetaryContext\":{\"amountType\":\"org.javamoney.moneta.Money\",\"empty\":false,\"fixedScale\":false,\"maxScale\":63,\"precision\":0},\"maximalMonetaryContext\":{\"amountType\":\"org.javamoney.moneta.Money\",\"empty\":false,\"fixedScale\":false,\"maxScale\":-1,\"precision\":0}},\"negative\":false,\"negativeOrZero\":false,\"number\":{\"amountFractionDenominator\":1000,\"amountFractionNumerator\":789,\"numberType\":\"java.math.BigDecimal\",\"precision\":6,\"scale\":3},\"numberStripped\":321.789,\"positive\":true,\"positiveOrZero\":true,\"zero\":false}";

        Money money = JSON.parseObject(json, Money.class);
        assertEquals(Monetary.getCurrency("EUR"), money.getCurrency());
        assertEquals(new BigDecimal("321.789"), money.getNumber().numberValue(BigDecimal.class));
    }
}
