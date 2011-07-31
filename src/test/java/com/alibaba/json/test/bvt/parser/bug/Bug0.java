package com.alibaba.json.test.bvt.parser.bug;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Bug0 extends TestCase {

    public void test_0() throws Exception {
        String text = "{a:1,b:2}";
        JSONObject json = JSON.parseObject(text, JSONObject.class);
        Assert.assertEquals(1, json.getIntValue("a"));
        Assert.assertEquals(2, json.getIntValue("b"));
    }

    public void test_array() throws Exception {
        String text = "[1, 2]}";
        JSONArray json = JSON.parseObject(text, JSONArray.class);
        Assert.assertEquals(1, json.getIntValue(0));
        Assert.assertEquals(2, json.getIntValue(1));
    }
}
