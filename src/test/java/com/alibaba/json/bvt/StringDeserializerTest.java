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

    public void test_StringBuffer() throws Exception {
        Assert.assertTrue(equals(new StringBuffer("123"), JSON.parseObject("123", StringBuffer.class)));
        Assert.assertTrue(equals(new StringBuffer("true"), JSON.parseObject("true", StringBuffer.class)));
        Assert.assertEquals(null, JSON.parseObject("null", StringBuffer.class));
    }

    public void test_StringBuilder() throws Exception {
        Assert.assertTrue(equals(new StringBuilder("123"), JSON.parseObject("123", StringBuilder.class)));
        Assert.assertTrue(equals(new StringBuilder("true"), JSON.parseObject("true", StringBuilder.class)));
        Assert.assertEquals(null, JSON.parseObject("null", StringBuilder.class));
    }

    private boolean equals(StringBuffer sb1, StringBuffer sb2) {
        if (sb1 == null && sb2 == null) {
            return true;
        }
        if ((sb1 == null && sb2 != null) || (sb1 != null && sb2 == null)) {
            return false;
        }

        return sb1.toString().equals(sb2.toString());
    }

    private boolean equals(StringBuilder sb1, StringBuilder sb2) {
        if (sb1 == null && sb2 == null) {
            return true;
        }
        if ((sb1 == null && sb2 != null) || (sb1 != null && sb2 == null)) {
            return false;
        }

        return sb1.toString().equals(sb2.toString());
    }
}
