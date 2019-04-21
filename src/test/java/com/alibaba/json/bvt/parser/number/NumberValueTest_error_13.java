package com.alibaba.json.bvt.parser.number;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class NumberValueTest_error_13 extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v0\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v1\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v2\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_3() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v3\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_5() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v5\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_6() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v6\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_7() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v7\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertTrue(error.getCause() instanceof ArithmeticException);
    }

    public void test_8() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v8\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertEquals(NumberFormatException.class, error.getCause().getClass());
    }

    public void test_9() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v9\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_10() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v10\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_11() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v11\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertEquals(ArithmeticException.class, error.getCause().getClass());
    }

    public void test_11_new() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v11\":new Date(49e99999999)}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_12() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v12\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
        assertEquals(ArithmeticException.class, error.getCause().getClass());
    }

    public void test_13() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v13\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_14() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v14\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_15() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v15\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_16() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v16\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_17() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v17\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_17_1() throws Exception {
        Exception error = null;
        try {
            JSONObject jsonObject = JSON.parseObject("{\"v17\":49e99999999}");
            jsonObject.getObject("v17", TimeUnit.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_18() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"v18\":49e99999999}", Model.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_20() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getIntValue("v");
        } catch (ArithmeticException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_20_long() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getLongValue("v");
        } catch (ArithmeticException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_21() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getDate("v");
        } catch (ArithmeticException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_22() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getObject("v", java.sql.Date.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_23() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getObject("v", Timestamp.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_24() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v", new BigDecimal("49e99999999"));
        Exception error = null;
        try {
            jsonObject.getObject("v", java.time.LocalDateTime.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_25() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"lineNumber\":49e99999999}");
        Exception error = null;
        try {
            jsonObject.toJavaObject(StackTraceElement.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_26() throws Exception {
        JSONObject jsonObject = JSON.parseObject("{\"v\":49e99999999}");
        Exception error = null;
        try {
            jsonObject.getObject("v", java.sql.Time.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public static class Model {
        public byte v0;
        public short v1;
        public int v2;
        public long v3;
        public Byte v4;

        public Short v5;
        public Integer v6;
        public Long v7;
        public BigInteger v8;

        public Timestamp v9;
        public java.sql.Date v10;
        public java.util.Date v11;
        public java.util.Calendar v12;
        public Timestamp v13;
        public java.time.LocalDateTime v14;

        public boolean v15;
        public Boolean v16;
        public TimeUnit v17;
        public java.sql.Time v18;
    }

    public static class M1 {
        public BigDecimal val;

        public M1(BigDecimal val) {
            this.val = val;
        }
    }
}
