package com.alibaba.json.bvt.parser.error;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class ParseErrorTest_1_v6 extends TestCase {
    protected void setUp() throws Exception {
        Field field = JSONLexer.class.getDeclaredField("V6");
        field.setAccessible(true);
        field.setBoolean(null, true);;
    }
    
    protected void tearDown() throws Exception {
        Field field = JSONLexer.class.getDeclaredField("V6");
        field.setAccessible(true);
        field.setBoolean(null, false);;
    }
    
    public void test_for_error() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"value\":\"xx\"");
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
