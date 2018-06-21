package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ParseNullTest extends TestCase {
    public void test_parse_null() throws Exception {
        JSON.parseObject("{\"value\":null}", Model.class);
    }
    
    public static class Model {
        public JSONObject value;
    }
}
