package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class ObjectArraySerializerTest extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] { "a12", "b34" });

        Assert.assertEquals("[\"a12\",\"b34\"]", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] {});

        Assert.assertEquals("[]", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] { null, null });

        Assert.assertEquals("[null,null]", out.toString());
    }

    public void test_3() throws Exception {
        Assert.assertEquals("[null,null]", JSON.toJSONString(new Object[] { null, null }, false));
    }
}
