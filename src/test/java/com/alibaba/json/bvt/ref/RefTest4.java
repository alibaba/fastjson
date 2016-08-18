package com.alibaba.json.bvt.ref;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest4 extends TestCase {

    public void test_str() throws Exception {
        Object[] array = new Object[2];
        array[0] = "abc";
        array[1] = array[0];
        Assert.assertEquals("[\"abc\",\"abc\"]", JSON.toJSONString(array));
    }

    public void test_decimal() throws Exception {
        Object[] array = new Object[2];
        array[0] = new BigDecimal("123");
        array[1] = array[0];
        Assert.assertEquals("[123,123]", JSON.toJSONString(array));
    }
    
    public void test_integer() throws Exception {
        Object[] array = new Object[2];
        array[0] = Integer.valueOf(123);
        array[1] = array[0];
        Assert.assertEquals("[123,123]", JSON.toJSONString(array));
    }
}
