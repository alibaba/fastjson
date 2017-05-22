package com.alibaba.json.bvt.path;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_containsValue_bigdecimal extends TestCase {
    public void test_root() throws Exception {
        Model model = new Model();
        model.value = new BigDecimal("1001");
        
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001L));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", (short) 1001));
        
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002L));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", (short) 1002));
    }
    
    public static class Model {
        public BigDecimal value;
    }
}
