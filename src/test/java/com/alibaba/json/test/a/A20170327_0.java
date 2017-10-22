package com.alibaba.json.test.a;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import junit.framework.TestCase;

import java.math.BigDecimal;

/**
 * Created by wenshao on 27/03/2017.
 */
public class A20170327_0 extends TestCase {
    public void test_0() throws Exception {
        String s = "{\"itemCurrentAmount\":{\"amount\":12.50,\"cent\":1250,\"centFactor\":100,\"currency\":\"CNY\",\"currencyCode\":\"CNY\"},\"itemDiscountAmount\":{\"$ref\":\"$.itemCurrentAmount\"}}";
        //String s = "{\"itemDiscountAmount\":{\"$ref\":\"$.itemCurrentAmount\"},\"itemCurrentAmount\":{\"amount\":12.50,\"cent\":1250,\"centFactor\":100,\"currency\":\"CNY\",\"currencyCode\":\"CNY\"}}";

        ParserConfig config = new ParserConfig();
        config.putDeserializer(Money.class, new MoneyDeserialize());

        Model model = JSON.parseObject(s, Model.class, config);
        assertSame(model.itemCurrentAmount, model.itemDiscountAmount);
        //JSONObject jsonObject = (JSONObject) JSON.parse(s);
        //assertSame(jsonObject.get("itemCurrentAmount"), jsonObject.get("itemDiscountAmount"));
    }

    public static class Model {
        public Money itemCurrentAmount;
        public Money itemDiscountAmount;
    }

    public static class Money {
        public BigDecimal amount;
        public long cent;
        public int centFactor;
        public String currency;
        public String currencyCode;
    }

    public static class MoneyDeserialize implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            ParseContext cxt = parser.getContext();
            Object object = parser.parse(fieldName);
            if (object == null) {
                return null;
            }
            String moneyCentStr = null;
            if (object instanceof JSONObject) {//历史数据兼容
                JSONObject jsonObject = (JSONObject) object;
                moneyCentStr = jsonObject.getString("cent");
            } else if (object instanceof String) {
                moneyCentStr = (String) object;
            } else {
                throw new RuntimeException("money属性反序列化失败，不支持的类型：" + object.getClass().getName());
            }
            if (moneyCentStr.length() != 0) {
                Money m = new Money();
                m.cent = Long.valueOf(moneyCentStr);
                return (T) m;
            }
            return null;
        }

        public int getFastMatchToken() {
            return 0;
        }
    }
}
