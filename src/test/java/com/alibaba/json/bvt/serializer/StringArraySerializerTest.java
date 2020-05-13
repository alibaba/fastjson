package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class StringArraySerializerTest extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new String[] { "a12", "b34" });

        Assert.assertEquals("[\"a12\",\"b34\"]", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new String[] { "a12", "\na\nb\nc\nd\"'", "b34" });

        Assert.assertEquals("[\"a12\",\"\\na\\nb\\nc\\nd\\\"'\",\"b34\"]", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new String[] { "a12", null });

        Assert.assertEquals("[\"a12\",null]", out.toString());
    }

    public void test_3() throws Exception {
        SerializeWriter out = new SerializeWriter(1024);

        JSONSerializer.write(out, new String[] { "a12", null });

        Assert.assertEquals("[\"a12\",null]", out.toString());
    }
}
