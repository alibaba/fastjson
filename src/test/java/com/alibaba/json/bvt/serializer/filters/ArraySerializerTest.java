package com.alibaba.json.bvt.serializer.filters;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.IOUtils;

public class ArraySerializerTest extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new A[] { new A(), null, null });

        Assert.assertEquals("[{},null,null]", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new A[] {});

        Assert.assertEquals("[]", out.toString());

        new IOUtils();
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new A[] { new A() });

        Assert.assertEquals("[{}]", out.toString());

        new IOUtils();
    }

    public static class A {

    }
}
