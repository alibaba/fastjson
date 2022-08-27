package com.alibaba.json.bvt.parser.number;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class NumberValueTest_error_7 extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            String text = "{\"value\":-";
            JSON.parse(text);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
