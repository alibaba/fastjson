package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.IOUtils;

public class ByteArraySerializerTest extends TestCase {

    public void test_b_0() {
        char[] buf = new char[4];
        IOUtils.getChars((byte) -127, 4, buf);
    }

    public void test_0() {
        Assert.assertEquals("\"\"", JSON.toJSONString(new byte[0]));
        Assert.assertEquals("\"AQI=\"", JSON.toJSONString(new byte[] { 1, 2 }));
        Assert.assertEquals("\"AQID\"", JSON.toJSONString(new byte[] { 1, 2, 3 }));

        Assert.assertEquals("1", JSON.toJSONString((byte) 1));
        Assert.assertEquals("1", JSON.toJSONString((short) 1));
        Assert.assertEquals("true", JSON.toJSONString(true));
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeByteArray(new byte[] { 1, 2, 3 });
        Assert.assertEquals("\"AQID\"", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(100);
        out.writeByteArray(new byte[] { 1, 2, 3 });
        Assert.assertEquals("\"AQID\"", out.toString());
    }
}
