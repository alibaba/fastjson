package com.alibaba.json.bvt.date;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class DateTest_error extends TestCase {

    
    public void test_error() throws Exception {
        String text = "{\"value\":true}";

        Exception error = null;
        try {
            JSON.parseObject(text, Date.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_1() throws Exception {
        String text = "{1:true}";

        Exception error = null;
        try {
            JSON.parseObject(text, Date.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        String text = "{\"@type\":\"java.util.Date\",\"value\":true}";

        Exception error = null;
        try {
            JSON.parseObject(text, Date.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        String text = "{\"@type\":\"java.util.Date\",\"value\":true}";

        Exception error = null;
        try {
            JSON.parseObject(text);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_4() throws Exception {
        String text = "{\"@type\":\"java.util.Date\",1:true}";

        Exception error = null;
        try {
            JSON.parseObject(text);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_5() throws Exception {
        String text = "\"xxxxxxxxx\"";

        Exception error = null;
        try {
            JSON.parseObject(text, Date.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
