package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_containsValue_double extends TestCase {
    public void test_root() throws Exception {
        Model model = new Model();
        model.value = 1001D;
        
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001L));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", (short) 1001));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001F));
        Assert.assertTrue(JSONPath.containsValue(model, "/value", 1001D));
        
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002L));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", (short) 1002));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002F));
        Assert.assertFalse(JSONPath.containsValue(model, "/value", 1002D));
    }
    
    public static class Model {
        public double value;
    }
}
