package com.alibaba.json.bvt.parser.deser;

import java.awt.Font;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.AwtCodec;

import junit.framework.TestCase;


public class FontDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        AwtCodec.instance.getFastMatchToken();
        
        Assert.assertNull(JSON.parseObject("null", StackTraceElement.class));
        Assert.assertNull(JSON.parseArray("null", StackTraceElement.class));
        Assert.assertNull(JSON.parseArray("[null]", StackTraceElement.class).get(0));
        Assert.assertNull(JSON.parseObject("{\"value\":null}", VO.class).getValue());
    }
    
    public void test_stack_error_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[]", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{33:22}", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"name\":22}", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"style\":true}", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"size\":\"33\"}", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_5() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"xxx\":22}", Font.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public static class VO {

        private Font value;

        public Font getValue() {
            return value;
        }

        public void setValue(Font value) {
            this.value = value;
        }

    }
}
