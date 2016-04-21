package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class NumberValueTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"value\":3D}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertTrue(3D == obj.getDouble("value").doubleValue()); 

    }
}
