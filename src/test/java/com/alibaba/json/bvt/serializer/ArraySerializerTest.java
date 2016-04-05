package com.alibaba.json.bvt.serializer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import junit.framework.TestCase;

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
