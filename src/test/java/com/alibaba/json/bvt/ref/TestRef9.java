package com.alibaba.json.bvt.ref;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class TestRef9 extends TestCase {
    public void test_array() throws Exception {
        String text = "{\"b\":{},\"a\":{\"$ref\":\"$.b\"}}";
        
        JSONObject obj = JSON.parseObject(text);
        JSONObject a = obj.getJSONObject("a");
        JSONObject b = obj.getJSONObject("b");
        Assert.assertSame(a, b);
    }
}
