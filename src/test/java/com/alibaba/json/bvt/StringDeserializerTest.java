package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class StringDeserializerTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("123", JSON.parseObject("123", String.class));
        Assert.assertEquals("true", JSON.parseObject("true", String.class));
        Assert.assertEquals(null, JSON.parseObject("null", String.class));
    }
}
