package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class NumberValueTest3 extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"value\":-21474836481}";
        JSONObject obj = (JSONObject) JSON.parse(text);
        Assert.assertEquals(Long.valueOf(-21474836481L), obj.get("value"));

    }
}
