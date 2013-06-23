package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class JSONScannerTest_scanFieldBoolean extends TestCase {

    public void test_true() throws Exception {
        String text = "{\"value\":true}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(true, obj.getValue());
    }

    public void test_false() throws Exception {
        String text = "{\"value\":false}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(false, obj.getValue());
    }

    public void test_1() throws Exception {
        String text = "{\"value\":\"true\"}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(true, obj.getValue());
    }

    public void test_2() throws Exception {
        String text = "{\"value\":\"false\"}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(false, obj.getValue());
    }

    public void test_3() throws Exception {
        String text = "{\"value\":\"1\"}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(true, obj.getValue());
    }
    

    
    public void test_5() throws Exception {
        String text = "{\"value\":false}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertEquals(false, obj.getValue());
    }

    public void test_error_0() {
        Exception error = null;
        try {
            String text = "{\"value\":true\\n\"";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() {
        Exception error = null;
        try {
            String text = "{\"value\":a";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() {
        Exception error = null;
        try {
            String text = "{\"value\":teue}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() {
        Exception error = null;
        try {
            String text = "{\"value\":tree}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() {
        Exception error = null;
        try {
            String text = "{\"value\":truu}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() {
        Exception error = null;
        try {
            String text = "{\"value\":fflse}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_6() {
        Exception error = null;
        try {
            String text = "{\"value\":fasse}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_7() {
        Exception error = null;
        try {
            String text = "{\"value\":falee}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_8() {
        Exception error = null;
        try {
            String text = "{\"value\":falss}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_9() {
        Exception error = null;
        try {
            String text = "{\"value\":false]";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_10() {
        Exception error = null;
        try {
            String text = "{\"value\":false}{";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_11() {
        Exception error = null;
        try {
            String text = "{\"value\":false}}";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_12() {
        Exception error = null;
        try {
            String text = "{\"value\":false}]";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_13() {
        Exception error = null;
        try {
            String text = "{\"value\":false},";
            JSON.parseObject(text, VO.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private boolean value;

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}
