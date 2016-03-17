package com.alibaba.json.bvt.parser.deser;

import java.awt.Font;
import java.awt.Rectangle;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.AwtCodec;

import junit.framework.TestCase;


public class RectangleDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        AwtCodec.instance.getFastMatchToken();
        
        Assert.assertNull(JSON.parseObject("null", Rectangle.class));
        Assert.assertNull(JSON.parseArray("null", Rectangle.class));
        Assert.assertNull(JSON.parseArray("[null]", Rectangle.class).get(0));
        Assert.assertNull(JSON.parseObject("{\"value\":null}", VO.class).getValue());
    }
    
    public void test_stack_error_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[]", Rectangle.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{33:22}", Rectangle.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"name\":22}", Rectangle.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"style\":true}", Rectangle.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_stack_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"size\":\"33\"}", Rectangle.class);
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

        private Rectangle value;

        public Rectangle getValue() {
            return value;
        }

        public void setValue(Rectangle value) {
            this.value = value;
        }

    }
}
