package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

import java.util.Currency;

public class CurrencyTest5 extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.put(Currency.class
                , config.createJavaBeanSerializer(Currency.class));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", Currency.getInstance("CNY"));

        String text = JSON.toJSONString(jsonObject, config);
        assertEquals("{\"value\":{\"currencyCode\":\"CNY\",\"displayName\":\"Chinese Yuan\",\"symbol\":\"CNY\"}}", text);

        Currency currency = JSON.parseObject(text, VO.class).value;

        assertSame(Currency.getInstance("CNY"), currency);
    }

    public static class VO {
        public Currency value;

    }
}
