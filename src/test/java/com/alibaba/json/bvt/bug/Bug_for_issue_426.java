package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_426 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{value:\"264,010,000.00\"}";
        Model model = JSON.parseObject(text, Model.class);
        Assert.assertTrue(264010000.00D == model.value);
    }
    
    public void test_for_issue_float() throws Exception {
        String text = "{value:\"264,010,000\"}";
        ModelFloat model = JSON.parseObject(text, ModelFloat.class);
        Assert.assertTrue(264010000F == model.value);
    }
    
    public void test_for_issue_int() throws Exception {
        String text = "{value:\"264,010,000\"}";
        ModelInt model = JSON.parseObject(text, ModelInt.class);
        Assert.assertTrue(264010000D == model.value);
    }
    
    
    public void test_for_issue_long() throws Exception {
        String text = "{value:\"264,010,000\"}";
        ModelLong model = JSON.parseObject(text, ModelLong.class);
        Assert.assertTrue(264010000D == model.value);
    }

    public static class Model {
        public double value;
    }
    
    public static class ModelFloat {
        public float value;
    }
    
    public static class ModelInt {
        public int value;
    }
    
    public static class ModelLong {
        public long value;
    }
}
