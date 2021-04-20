package com.alibaba.json.bvt.bug;

import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

import static com.alibaba.fastjson.parser.ParserConfig.AUTO_SUPPORT;

public class Bug_for_Issue_603 extends TestCase {
    public void setUp() {
        ParserConfig.getGlobalInstance().putDeserializer(OrderActionEnum.class, new OrderActionEnumDeser());
    }

    public void tearDown() {
        ParserConfig.getGlobalInstance().clearDeserializers();
        ParserConfig.global = new ParserConfig();
    }
    public void test_for_issue() throws Exception {


        {
            Msg msg = JSON.parseObject("{\"actionEnum\":1,\"body\":\"A\"}", Msg.class);
            Assert.assertEquals(msg.body, "A");
            Assert.assertEquals(msg.actionEnum, OrderActionEnum.FAIL);
        }
        {
            Msg msg = JSON.parseObject("{\"actionEnum\":0,\"body\":\"B\"}", Msg.class);
            Assert.assertEquals(msg.body, "B");
            Assert.assertEquals(msg.actionEnum, OrderActionEnum.SUCC);
        }
    }

    public static class OrderActionEnumDeser implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            Integer intValue = parser.parseObject(int.class);
            if (intValue == 1) {
                return (T) OrderActionEnum.FAIL;
            } else if (intValue == 0) {
                return (T) OrderActionEnum.SUCC;
            }
            throw new IllegalStateException();
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }

    }

    public static enum OrderActionEnum {
                                        FAIL(1), SUCC(0);

        private int code;

        OrderActionEnum(int code){
            this.code = code;
        }
    }

    public static class Msg {

        public OrderActionEnum actionEnum;
        public String          body;
    }
}
