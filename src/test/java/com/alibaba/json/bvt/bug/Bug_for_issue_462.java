package com.alibaba.json.bvt.bug;

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
}
