package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_primitive_int extends TestCase {

    public void test_emptyStr() throws Exception {
        JSON.parseObject("{\"value\":\"\"}", VO.class);
    }
    
    public void test_null() throws Exception {
        JSON.parseObject("{\"value\":null}", VO.class);
    }
    
    public void test_strNull() throws Exception {
        JSON.parseObject("{\"value\":\"null\"}", VO.class);
    }

    public static class VO {

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            throw new UnsupportedOperationException();
        }

    }
}
