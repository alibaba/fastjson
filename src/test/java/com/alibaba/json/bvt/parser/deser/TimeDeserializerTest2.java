package com.alibaba.json.bvt.parser.deser;

import java.awt.Color;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import org.junit.Assert;
import junit.framework.TestCase;


public class TimeDeserializerTest2 extends TestCase {
    public void test_0 () throws Exception {
        long millis = System.currentTimeMillis();
        JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":" + millis + "}");
    }
    
    public void test_error() throws Exception {
        long millis = System.currentTimeMillis();
        
        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",33:" + millis + "}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":true}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        long millis = System.currentTimeMillis();
        
        Exception error = null;
        try {
            JSON.parse("{\"@type\":\"java.sql.Time\",\"value\":" + millis + ",}");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
