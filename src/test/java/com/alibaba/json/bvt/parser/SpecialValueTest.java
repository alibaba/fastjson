package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class SpecialValueTest extends TestCase {
    public void test_special_value() throws Exception {
        String text = "{\"ab\\\"cde\\\"fghijkl\":\"12345\\\"6\\\"7890\"}";
        JSON.parseObject(text);
    }
}
