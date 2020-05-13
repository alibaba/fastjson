package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONScannerTest_scanFieldFloat extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"value\":1.0}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(1F == obj.getValue());
    }

    @SuppressWarnings("resource")
    public void test_isBlank() throws Exception {
        String text = "   {\"value\":1.0}";
        Assert.assertTrue(!new JSONScanner(text).isBlankInput());
    }

    public void test_1() throws Exception {
        String text = "{\"value\":\"1\"}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(1F == obj.getValue());
    }

    public void test_2() throws Exception {
        String text = "{\"f1\":2,\"value\":1.0}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(1F == obj.getValue());
    }

    public void test_3() throws Exception {
        String text = "{\"value\":1.01}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(1.01F == obj.getValue());
    }

    public void test_4() throws Exception {
        String text = "{\"value\":1.}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(1F == obj.getValue());
    }

    public void test_error_1() throws Exception {
        String text = "{\"value\":922337203685477580723}";
        VO obj = JSON.parseObject(text, VO.class);
        Assert.assertTrue(922337203685477580723F == obj.getValue());
    }

    public void test_error_2() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":32M}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":32}{";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":中}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.F";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_6() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.2]";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_7() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.2}]";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_8() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.2}}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_9() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.2},";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_10() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.\\0}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_11() throws Exception {
        JSONException error = null;
        try {
            String text = "{\"value\":3.中}";
            JSON.parseObject(text, VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private float value;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

    }
}
