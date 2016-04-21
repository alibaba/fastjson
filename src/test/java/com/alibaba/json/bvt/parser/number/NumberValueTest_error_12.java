package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class NumberValueTest_error_12 extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            String text = "{\"value\":33.33";
            JSON.parse(text);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
