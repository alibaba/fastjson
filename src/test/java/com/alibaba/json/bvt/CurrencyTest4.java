package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import junit.framework.TestCase;

import java.util.Currency;
import java.util.Locale;

public class CurrencyTest4 extends TestCase {

    public void test_0() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currency", "CNY");

        String text = JSON.toJSONString(jsonObject);

        Currency currency = JSON.parseObject(text, Currency.class);

        assertSame(Currency.getInstance("CNY"), currency);
    }

    public void test_1() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currencyCode", "CNY");

        String text = JSON.toJSONString(jsonObject);

        Currency currency = JSON.parseObject(text, Currency.class);

        assertSame(Currency.getInstance("CNY"), currency);
    }

    public static class VO {
        public Currency value;

    }
}
