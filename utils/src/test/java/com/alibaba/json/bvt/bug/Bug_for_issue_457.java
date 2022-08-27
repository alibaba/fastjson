package com.alibaba.json.bvt.bug;

import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import junit.framework.TestCase;

public class Bug_for_issue_457 extends TestCase {

    public void test_for_issue() throws Exception {
        ParserConfig.global.putDeserializer(MyEnum.class, new MyEnumDeser());
        VO entity = JSON.parseObject("{\"myEnum\":\"AA\"}", VO.class);
        Assert.assertEquals(MyEnum.A, entity.myEnum);
    }
    
    public static class MyEnumDeser implements ObjectDeserializer {

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String text = (String) parser.parse();
            if (text.equals("AA")) {
                return (T) MyEnum.A;
            }
            
            if (text.equals("BB")) {
                return (T) MyEnum.B;
            }
            return null;
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_STRING;
        }
        
    }


    public static class VO {

        private MyEnum myEnum;

        public void setMyEnum(MyEnum myEnum) {
            this.myEnum = myEnum;
        }

        public MyEnum getMyEnum() {
            return myEnum;
        }
    }

    public static enum MyEnum {
                               A, B
    }
}
