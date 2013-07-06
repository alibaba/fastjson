package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JSONLexerTest_3 extends TestCase {

    public void test_matchField() throws Exception {
        JSON.parseObject("{\"val\":{}}", VO.class);
    }

    public static class VO {

        private A value;

        public A getValue() {
            return value;
        }

        public void setValue(A value) {
            this.value = value;
        }

    }
    
    public static class A {
        
    }
}
