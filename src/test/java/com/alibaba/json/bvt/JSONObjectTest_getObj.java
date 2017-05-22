package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class JSONObjectTest_getObj extends TestCase {

    public void test_get_empty() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("value", "");
        Assert.assertEquals("", obj.get("value"));
        Assert.assertNull(obj.getObject("value", Model.class));
    }
    
    public void test_get_null() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("value", "null");
        Assert.assertEquals("null", obj.get("value"));
        Assert.assertNull(obj.getObject("value", Model.class));
    }
    
    public static class Model {
        
    }
}
