package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class NumberValueTest_error_10 extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            String text = "{\"value\":3e-";
            JSON.parse(text);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
