package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class ParseNullTest extends TestCase {
    public void test_parse_null() throws Exception {
        Model model = JSON.parseObject("{\"value\":null}", Model.class);
        Assert.assertNull(model.value);
    }
    
    public static class Model {
        public JSONObject value;
    }
}
