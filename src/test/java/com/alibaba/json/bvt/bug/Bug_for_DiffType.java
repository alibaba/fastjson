package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_DiffType extends TestCase {
    public void test_for_diff_type() throws Exception {
        Model model = new Model();
        model.setValue(1001);
        
        String text = JSON.toJSONString(model);
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.value, model2.value);
    }
    
    public static class Model {
        public String value;
        
        public long getValue() {
            return Long.parseLong(value);
        }
        
        public void setValue(long value) {
            this.value = Long.toString(value);
        }
    }
}
