package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class PrimitiveTest extends TestCase {

    public void test_0() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, (byte) 1);

        Assert.assertEquals("1", out.toString());
    }

    public void test_0_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, (byte) 1);

        Assert.assertEquals("1", out.toString());
    }

    public void test_1() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, (short) 1);

        Assert.assertEquals("1", out.toString());
    }

    public void test_1_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, (short) 1);

        Assert.assertEquals("1", out.toString());
    }

    public void test_2() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, true);

        Assert.assertEquals("true", out.toString());
    }

    public void test_2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, true);

        Assert.assertEquals("true", out.toString());
    }

    public void test_3() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, false);

        Assert.assertEquals("false", out.toString());
    }

    public void test_3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, false);

        Assert.assertEquals("false", out.toString());
    }

    public void test_4() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, new boolean[] { true, false });

        Assert.assertEquals("[true,false]", out.toString());
    }

    public void test_4_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, new boolean[] { true, false });

        Assert.assertEquals("[true,false]", out.toString());
    }

    public void test_5() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, new boolean[] {});

        Assert.assertEquals("[]", out.toString());
    }

    public void test_5_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, new boolean[] {});

        Assert.assertEquals("[]", out.toString());
    }

    public void test_6() throws Exception {
        StringWriter out = new StringWriter();

        JSONSerializer.write(out, new boolean[] { true, false, true });

        Assert.assertEquals("[true,false,true]", out.toString());
    }

    public void test_6_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer.write(out, new boolean[] { true, false, true });

        Assert.assertEquals("[true,false,true]", out.toString());
    }
}
