package com.alibaba.json.bvt.bug;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_462 extends TestCase {
    public void test_int() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":1001}");
        Object value = object.get("value");
        Assert.assertEquals(Integer.class, value.getClass());
    }
    
    public void test_long() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":2147483649}");
        Object value = object.get("value");
        Assert.assertEquals(Long.class, value.getClass());
    }
    
    public void test_bigint() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":9223372036854775808}");
        Object value = object.get("value");
        Assert.assertEquals(BigInteger.class, value.getClass());
    }

    public void test_bigdecimal() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":9223372036854.775808}");
        Object value = object.get("value");
        Assert.assertEquals(BigDecimal.class, value.getClass());
        assertEquals("9223372036854.775808", ((BigDecimal) value).toPlainString());
    }

    public void test_bigdecimal_1() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":1.01}");
        Object value = object.get("value");
        Assert.assertEquals(BigDecimal.class, value.getClass());
        assertEquals("1.01", ((BigDecimal) value).toPlainString());
    }

    public void test_bigdecimal_2() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":11.012345}");
        Object value = object.get("value");
        Assert.assertEquals(BigDecimal.class, value.getClass());
        assertEquals("11.012345", ((BigDecimal) value).toPlainString());
    }

    public void test_bigdecimal_3() throws Exception {
        JSONObject object = JSON.parseObject("{\"value\":126.995801}");
        Object value = object.get("value");
        Assert.assertEquals(BigDecimal.class, value.getClass());
        assertEquals("126.995801", ((BigDecimal) value).toPlainString());
    }


}
