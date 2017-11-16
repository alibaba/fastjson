package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.parser.Feature;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ParseEmptyMap extends TestCase {
    public void test_parse_null() throws Exception {
        Model model = JSON.parseObject("{\"value\":{\"@type\":\"java.util.LinkedHashMap\"}}", Model.class, Feature.SupportAutoType);
        Assert.assertNotNull(model.value);
        Assert.assertTrue(model.value instanceof java.util.LinkedHashMap);
    }
    
    public static class Model {
        public Object value;
    }
}
