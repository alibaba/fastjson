package com.alibaba.json.bvt.parser.error;

import java.util.Date;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import junit.framework.TestCase;

public class ParseErrorTest_date extends TestCase {

    public void test_for_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":\"2011-01-09M\"}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":\"2011-01-09", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
//    public void test_for_error_2() throws Exception {
//        Exception error = null;
//        try {
//            JSON.parseObject("{\"value\":\"2011-01-09 00:00:00.000+.M\"}", Model.class);
//        } catch (JSONException ex) {
//            error = ex;
//        }
//        Assert.assertNotNull(error);
//    }
//    
//    public void test_for_error_3() throws Exception {
//        Exception error = null;
//        try {
//            JSON.parseObject("{\"value\":\"2011-01-09 00:00:00.000+2M\"}", Model.class);
//        } catch (JSONException ex) {
//            error = ex;
//        }
//        Assert.assertNotNull(error);
//    }
    
    public static class Model {
        public Date value;
    }
    
}
