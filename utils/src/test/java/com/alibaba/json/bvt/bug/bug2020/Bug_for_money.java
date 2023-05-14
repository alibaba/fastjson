package com.alibaba.json.bvt.bug.bug2020;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Bug_for_money extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject obj = JSON.parseObject(
                "{\"productMaxPriceAmt\":{\"amount\":4.99,\"centFactor\":100,\"cent\":499,\"currency\":\"USD\","
                        + "\"currencyCode\":\"USD\"}}");
        Money money = obj.getObject("productMaxPriceAmt", Money.class);
        assertEquals("USD", money.getCurrencyCode());
        assertEquals(new BigDecimal("4.99"), money.getAmount());
    }

    public static class Money implements Serializable, Comparable {

        private static final long serialVersionUID = 6009335074727417445L;

        public static final String DEFAULT_CURRENCY_CODE = "CNY";

        public static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

        private static final int[] centFactors = new int[] { 1, 10, 100, 1000 };

        private long cent;

        private Currency currency;

        private String currencyCode;

        public Money() {
            this(0);
        }

        private Money(double amount) {
            this(amount, Currency.getInstance(DEFAULT_CURRENCY_CODE));
        }


        public Money(double amount, Currency currency) {
            this.currency = currency;
            this.cent = Math.round(amount * getCentFactor());
        }

        public BigDecimal getAmount() {
            return BigDecimal.valueOf(cent, currency.getDefaultFractionDigits());
        }


        public long getCent() {
            return cent;
        }


        public Currency getCurrency() {
            return currency;
        }


        public int getCentFactor() {
            return centFactors[currency.getDefaultFractionDigits()];
        }


        public boolean equals(Object other) {
            return (other instanceof Money) && equals((Money) other);
        }

        public boolean equals(Money other) {
            return currency.equals(other.currency) && (cent == other.cent);
        }

        public int hashCode() {
            return (int) (cent ^ (cent >>> 32));
        }

        public int compareTo(Object other) {
            return compareTo((Money) other);
        }

        public int compareTo(Money other) {
            assertSameCurrencyAs(other);

            if (cent < other.cent) {
                return -1;
            } else if (cent == other.cent) {
                return 0;
            } else {
                return 1;
            }
        }

        public String toString() {
            return getAmount().toString();
        }

        protected void assertSameCurrencyAs(Money other) {
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("Money math currency mismatch.");
            }
        }

        public void setCent(long l) {
            cent = l;
        }

        public void setCurrencyCode(String currencyCode) {
            if (currencyCode != null && !currencyCode.isEmpty()) {
                this.currencyCode = currencyCode;
                currency = Currency.getInstance(currencyCode);
            }
        }

        public String getCurrencyCode() {
            currencyCode = currency.getCurrencyCode();
            return currencyCode;
        }
    }


}
