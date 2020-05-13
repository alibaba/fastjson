package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class DoubleArraySerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("[]", JSON.toJSONString(new double[0]));
        Assert.assertEquals("[null]", JSON.toJSONString(new double[] { Double.NaN }));
        Assert.assertEquals("[1.0,2.0]", JSON.toJSONString(new double[] { 1, 2 }));
        Assert.assertEquals("[1.0,2.0,3.0]", JSON.toJSONString(new double[] { 1, 2, 3 }));
        Assert.assertEquals("[1.0,2.0,3.0,null,null]", JSON.toJSONString(new double[] { 1, 2, 3, Double.NaN, Double.NaN }));
    }
}
