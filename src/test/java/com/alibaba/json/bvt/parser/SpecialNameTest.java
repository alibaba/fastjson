package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class SpecialNameTest extends TestCase {
    public void test_special_key() throws Exception {
        String text = "{\"name\\\\\\\"\":1001}";
        
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1001, obj.get("name\\\""));
    }
    
    public void test_special_key_1() throws Exception {
        String text = "{\"name\\\\\\\"\":1001}";
        
        Model obj = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1001, obj.value);
    }
    
    public static class Model {
        @JSONField(name="name\\\"")
        public int value;
    }
}
