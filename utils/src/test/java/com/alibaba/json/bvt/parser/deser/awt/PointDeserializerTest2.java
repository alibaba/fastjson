package com.alibaba.json.bvt.parser.deser.awt;

import java.awt.Point;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;


public class PointDeserializerTest2 extends TestCase {
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"z\":44}", Point.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
