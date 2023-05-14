package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;

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

    public void test_get_obj() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("value", new HashMap());
        Assert.assertEquals(new JSONObject(), obj.getObject("value", JSONObject.class));
    }

    public void test_get_obj2() throws Exception {
        List<JSONObject> json = JSON.parseArray("[{\"values\":[{}]}]", JSONObject.class);

        for (JSONObject obj : json) {
            Object values = obj.getObject("values", new TypeReference<List<JSONObject>>() {});
        }
    }
    
    public static class Model {
        
    }
}
