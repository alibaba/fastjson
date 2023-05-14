package com.alibaba.json.bvt.parser.error;

import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;

public class ParseErrorTest_21 extends TestCase {

    public void test_for_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":123}", Model.class);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{,,,\"id\",}}", Model.class);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{'child1':{\"id\":123}}}", Model.class, ParserConfig.getGlobalInstance(), 0);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{'child1',{\"id\":123}}}", Model.class);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{child1:{\"id\":123}}}", Model.class, ParserConfig.getGlobalInstance(), 0);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_for_error_5() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":{child1,{\"id\":123}}}", Model.class, ParserConfig.getGlobalInstance(), 0, Feature.AllowUnQuotedFieldNames);   
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public static class Model {
        public Map<String, Child> value;
    }
    
    public static class Child {
        public int id;
    }
}
