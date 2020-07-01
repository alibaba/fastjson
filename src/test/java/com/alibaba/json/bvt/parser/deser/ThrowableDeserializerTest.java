package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;


public class ThrowableDeserializerTest extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertEquals(Throwable.class, JSON.parseObject("{}", Throwable.class).getClass());
        Assert.assertEquals(Throwable.class, JSON.parseObject("{,,,}", Throwable.class).getClass());
        Assert.assertEquals(java.lang.RuntimeException.class, JSON.parseObject("{\"@type\":\"java.lang.RuntimeException\"}", Throwable.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{\"message\":null}", Throwable.class).getMessage());
        Assert.assertEquals(Exception.class, JSON.parseObject("{\"cause\":{}}", Throwable.class).getCause().getClass());
    }
    
    public void test_error() throws Exception {
        JSONException error = null;
        try {
            JSON.parseObject("{\"@type\":33}", Throwable.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error1() throws Exception {
        JSONException error = null;
        try {
            Assert.assertEquals(null, JSON.parseObject("{\"message\":33}", Throwable.class).getMessage());
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error2() throws Exception {
        Exception error = null;
        try {
            Assert.assertEquals(null, JSON.parseObject("{}", MyException.class).getMessage());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error3() throws Exception {
        Exception error = null;
        try {
            Assert.assertEquals(null, JSON.parseObject("{}", MyException2.class).getMessage());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public static class MyException extends Exception {
        private MyException() {
            
        }
    }
    
    public static class MyException2 extends Exception {
        public MyException2() {
            throw new RuntimeException();
        }
    }
}
