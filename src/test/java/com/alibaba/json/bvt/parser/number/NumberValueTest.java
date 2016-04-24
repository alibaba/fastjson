package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class NumberValueTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"value\":3D}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertTrue(3D == ((Double)obj.get("value")).doubleValue()); 

    }
    
    public void test_1() throws Exception {
        String text = "{\"value\":3.e3D}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertTrue(3.e3D == ((Double)obj.get("value")).doubleValue()); 

    }
}
