package com.alibaba.json.bvt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class JSONTest2 extends TestCase {

    public void test_0() throws Exception {
        StringReader reader = new StringReader("{a:1,b:2}");
        String text = IOUtils.toString(reader);
        JSONObject json = (JSONObject) JSON.parse(text);
        Assert.assertEquals(2, json.size());
        Assert.assertEquals(1, json.getIntValue("a"));
        Assert.assertEquals(2, json.getIntValue("b"));
    }

    public void test_1() throws Exception {
        InputStream input = new ByteArrayInputStream("{a:1,b:2}".getBytes());
        String text = IOUtils.toString(input);
        JSONObject json = (JSONObject) JSON.parse(text);
        Assert.assertEquals(2, json.size());
        Assert.assertEquals(1, json.getIntValue("a"));
        Assert.assertEquals(2, json.getIntValue("b"));
    }

    public void test_2() throws Exception {
        Assert.assertEquals(new Byte((byte) 1), JSON.parseObject("1", Byte.class));
        Assert.assertEquals(new Short((short) 1), JSON.parseObject("1", Short.class));
        Assert.assertEquals(new Integer((int) 1), JSON.parseObject("1", Integer.class));
        Assert.assertEquals(new Long((long) 1), JSON.parseObject("1", Long.class));
        Assert.assertEquals(new Float((float) 1), JSON.parseObject("1", Float.class));
        Assert.assertEquals(new Double((double) 1), JSON.parseObject("1", Double.class));
    }

    public void test_3() throws Exception {
        Assert.assertEquals(new Byte((byte) 1), JSON.parseObject("1", byte.class));
        Assert.assertEquals(new Short((short) 1), JSON.parseObject("1", short.class));
        Assert.assertEquals(new Integer((int) 1), JSON.parseObject("1", int.class));
        Assert.assertEquals(new Long((long) 1), JSON.parseObject("1", long.class));
        Assert.assertEquals(new Float((float) 1), JSON.parseObject("1", float.class));
        Assert.assertEquals(new Double((double) 1), JSON.parseObject("1", double.class));
    }

    public void test_4() throws Exception {
        Assert.assertEquals(new BigInteger("1"), JSON.parseObject("1", BigInteger.class));
        Assert.assertEquals(new BigDecimal("1"), JSON.parseObject("1", BigDecimal.class));
    }

    public void test_5() throws Exception {
        Assert.assertArrayEquals(new byte[] { 1 }, (byte[]) JSON.parseObject("[1]", byte[].class));
        Assert.assertArrayEquals(new short[] { 1 }, (short[]) JSON.parseObject("[1]", short[].class));
        Assert.assertArrayEquals(new int[] { 1 }, (int[]) JSON.parseObject("[1]", int[].class));
        Assert.assertArrayEquals(new long[] { 1 }, (long[]) JSON.parseObject("[1]", long[].class));
        float[] array1 = JSON.parseObject("[1]", float[].class);
        double[] array2 = JSON.parseObject("[1]", double[].class);
    }

    public void test_6() throws Exception {
        Assert.assertArrayEquals(new Byte[] { 1 }, (Byte[]) JSON.parseObject("[1]", Byte[].class));
        Assert.assertArrayEquals(new Short[] { 1 }, (Short[]) JSON.parseObject("[1]", Short[].class));
        Assert.assertArrayEquals(new Integer[] { 1 }, (Integer[]) JSON.parseObject("[1]", Integer[].class));
        Assert.assertArrayEquals(new Long[] { 1L }, (Long[]) JSON.parseObject("[1]", Long[].class));
        Float[] array1 = JSON.parseObject("[1]", Float[].class);
        Double[] array2 = JSON.parseObject("[1]", Double[].class);
    }
    
    public void test_7() throws Exception {
        Assert.assertNull(JSON.parseObject(null, new TypeReference<Integer>() {}.getType(), 0));
    }
}
