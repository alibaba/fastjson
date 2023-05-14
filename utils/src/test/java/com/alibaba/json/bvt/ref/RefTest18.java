package com.alibaba.json.bvt.ref;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class RefTest18 extends TestCase {
    public void test_array() throws Exception {
        String text = "{\"b\":{},\"a\":[{\"$ref\":\"$.b\"}]}";
        
        JSONObject obj = JSON.parseObject(text);
        JSONArray array = obj.getJSONArray("a");
        Assert.assertEquals(1, array.size());
        Assert.assertNotNull(array.get(0));
    }
}
