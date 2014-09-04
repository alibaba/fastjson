package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class AbstractTest extends TestCase {

    public void test_0() throws Exception {
        ParserConfig.getGlobalInstance().putDeserializer(A.class, new ADeserializer());
        VO vo = JSON.parseObject("{\"a\":{\"num\":1,\"name\":\"bb\"}}", VO.class);
        Assert.assertTrue(vo.getA() instanceof B);
    }
    
    public void test_1() throws Exception {
        ParserConfig.getGlobalInstance().putDeserializer(A.class, new ADeserializer());
        VO vo = JSON.parseObject("{\"a\":{\"num\":2,\"name\":\"bb\"}}", VO.class);
        Assert.assertTrue(vo.getA() instanceof C);
    }


    public static class ADeserializer implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            JSONObject json = parser.parseObject();
            int num = json.getInteger("num");
            if (num == 1) {
                return (T) JSON.toJavaObject(json, B.class);
            } else if (num == 2) {
                return (T) JSON.toJavaObject(json, C.class);
            } else {
                return (T) JSON.toJavaObject(json, A.class);
            }
        }

        public int getFastMatchToken() {
            return JSONToken.LBRACE;
        }

    }

    public static class VO {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }

    public static class A {

        private int num;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public static class B extends A {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class C extends A {

        public String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
