package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_9 extends TestCase {

    public void test_ident() throws Exception {
        JSON.parseObject("\"AAA\"", Type.class);
    }

    public void test_a() throws Exception {
        JSON.parseObject("{\"type\":\"AAA\"}", VO.class);
    }
    
    public void test_b() throws Exception {
        JSON.parseObject("{\"tt\":\"AA\"}", VO.class);
    }
    
    public void test_value() throws Exception {
        JSON.parseObject("{\"type\":'AAA'}", VO.class);
    }
    
    public void test_value2() throws Exception {
        JSON.parseObject("{\"type\":\"AAA\",id:0}", VO.class);
    }

    public static class VO {

        public VO(){

        }

        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

    }

    public static enum Type {
        AAA, BBB, CCC
    }
}
