package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class ShortArraySerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("[]", JSON.toJSONString(new short[0]));
        Assert.assertEquals("[1,2]", JSON.toJSONString(new short[] { 1, 2 }));
        Assert.assertEquals("[1,2,3]", JSON.toJSONString(new short[] { 1, 2, 3 }));
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeShortArray(new short[] { 1, 2, 3 });
        Assert.assertEquals("[1,2,3]", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(100);
        out.writeShortArray(new short[] { 1, 2, 3 });
        Assert.assertEquals("[1,2,3]", out.toString());
    }
}
