package com.alibaba.json.bvt.parser.deser.awt;

import java.awt.Point;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.AwtCodec;

import junit.framework.TestCase;


public class PointDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        new AwtCodec().getFastMatchToken();
    }
    
    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[]", Point.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{33:44}", Point.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"r\":44.}", Point.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"x\":\"aaa\"}", Point.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
