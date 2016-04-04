package com.alibaba.json.test;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class TestForShortString extends TestCase {
    public void test_0() throws Exception {
        String text = "{\"a\":\"b\"}";
        for (int i = 0; i < 10; ++i) {
            JSON.parseObject(text);
        }
    }
}
