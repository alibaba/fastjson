package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JSONScannerTest_scanFieldString_error extends TestCase {

    public void f_test_error_0() {
        Exception error = null;
        try {
            String text = "{\"value\":\"1\\n\"";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void f_test_error_1() {
        Exception error = null;
        try {
            String text = "{\"value\":\"1\"}}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() {
        Exception error = null;
        try {
            String text = "{\"value\":\"1\"}1";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() {
        Exception error = null;
        try {
            String text = "{\"value\":\"1\"1";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }


    public static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
