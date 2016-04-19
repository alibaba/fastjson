package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class SpecialNameTest2 extends TestCase {
    public void test_special_key() throws Exception {
        String text = "{\"name\\\\\":1001,\"value\":\"xxx\\\\\"}";
        
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(1001, obj.get("name\\"));
        Assert.assertEquals("xxx\\", obj.get("value"));
    }
    
    public void test_special_key_1() throws Exception {
        String text = "{\"name\\\\\":1001,\"value\":\"xxx\\\\\"}";
        
        Model obj = JSON.parseObject(text, Model.class);
        Assert.assertEquals(1001, obj.name);
        Assert.assertEquals("xxx\\", obj.value);
    }
    
    public static class Model {
        @JSONField(name="name\\")
        public int name;
        public String value;
    }
}
