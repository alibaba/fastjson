package com.alibaba.json.bvt.serializer;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest_11 extends TestCase {

    public void test_erro_0() throws Exception {
        SerializeWriter out = new SerializeWriter();
        out.write(true);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        out.writeTo(byteOut, "UTF-8");
        Assert.assertEquals("true", new String(byteOut.toByteArray()));
        out.close();
    }

}
