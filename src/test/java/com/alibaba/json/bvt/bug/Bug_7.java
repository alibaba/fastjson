package com.alibaba.json.bvt.bug;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class Bug_7 extends TestCase {

    public void test_floatArray() throws Exception {
        float[] a = new float[] { 1, 2 };
        String text = JSON.toJSONString(a);
        JSON json = (JSON) JSON.parse(text);
        Assert.assertEquals("[1.0,2.0]", json.toJSONString());
    }

    public void test_doubleArray() throws Exception {
        double[] a = new double[] { 1, 2 };
        String text = JSON.toJSONString(a);
        JSON json = (JSON) JSON.parse(text);
        Assert.assertEquals("[1.0,2.0]", json.toJSONString());
    }

    public void test_bigintegerArray() throws Exception {
        BigInteger[] a = new BigInteger[] { new BigInteger("214748364812"), new BigInteger("2147483648123") };
        String text = JSON.toJSONString(a);
        Assert.assertEquals("[214748364812,2147483648123]", text);
        JSON json = (JSON) JSON.parse(text);
        Assert.assertEquals("[214748364812,2147483648123]", json.toJSONString());
    }

    public void test_AtomicIntegerArray() throws Exception {
        AtomicIntegerArray array = new AtomicIntegerArray(3);
        array.set(0, 1);
        array.set(1, 2);
        array.set(2, 3);
        String text = JSON.toJSONString(array);
        Assert.assertEquals("[1,2,3]", text);
    }

    public void test_AtomicLongArray() throws Exception {
        AtomicLongArray array = new AtomicLongArray(3);
        array.set(0, 1);
        array.set(1, 2);
        array.set(2, 3);
        String text = JSON.toJSONString(array);
        Assert.assertEquals("[1,2,3]", text);
    }
}
   
