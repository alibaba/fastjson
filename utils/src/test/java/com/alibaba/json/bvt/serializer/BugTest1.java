package com.alibaba.json.bvt.serializer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BugTest1 extends TestCase {

    public void test_0() throws Exception {
        AtomicBoolean v = new AtomicBoolean();
        Assert.assertEquals("false", JSON.toJSONString(v));
    }

    public void test_1() throws Exception {
        AtomicBoolean v = new AtomicBoolean(true);
        Assert.assertEquals("true", JSON.toJSONString(v));
    }

    public void test_2() throws Exception {
        AtomicInteger v = new AtomicInteger();
        Assert.assertEquals("0", JSON.toJSONString(v));
    }

    public void test_3() throws Exception {
        AtomicLong v = new AtomicLong();
        Assert.assertEquals("0", JSON.toJSONString(v));
    }

    public void test_4() throws Exception {
        AtomicReference<Integer> v = new AtomicReference<Integer>(3);
        Assert.assertEquals("3", JSON.toJSONString(v));
    }

    public void test_5() throws Exception {
        Assert.assertEquals("\"java.util.concurrent.atomic.AtomicReference\"", JSON.toJSONString(AtomicReference.class));
    }

    public void test_7() throws Exception {
        Assert.assertEquals("'java.util.concurrent.atomic.AtomicReference'", JSON.toJSONString(AtomicReference.class, SerializerFeature.UseSingleQuotes));
    }
}
