package com.alibaba.json.bvt.parser.array;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class BeanToArrayTest_int extends TestCase {

    public void test_int() throws Exception {
        Model model = JSON.parseObject("[-100,100]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(-100L, model.v1);
        Assert.assertEquals(100L, model.v2);
    }
    
    public void test_int_space() throws Exception {
        Model model = JSON.parseObject("[-100 ,100 ]", Model.class, Feature.SupportArrayToBean);
        Assert.assertEquals(-100L, model.v1);
        Assert.assertEquals(100L, model.v2);
    }

    public void test_int_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[-", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_int_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[-1:", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_int_error_max() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[1,92233720368547758000}", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_bool_error_min() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[1,-92233720368547758000}", Model.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Model {
        public int v1;
        public int v2;
    }
}
