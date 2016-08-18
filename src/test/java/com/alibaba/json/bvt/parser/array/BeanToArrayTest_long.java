package com.alibaba.json.bvt.parser.array;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest_long extends TestCase {

    public void test_long() throws Exception {
        Model model = JSON.parseObject("[-100,100]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(-100L, model.v1);
        Assert.assertEquals(100L, model.v2);
    }
    
    public void test_long_space() throws Exception {
        Model model = JSON.parseObject("[-100 ,100 ]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(-100L, model.v1);
        Assert.assertEquals(100L, model.v2);
    }

    public void test_long_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[-", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_long_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[-1:", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_long_error_max() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[1,92233720368547758000}", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_long_error_min() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[1,-92233720368547758000}", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {
        public long v1;
        public long v2;
    }
}
