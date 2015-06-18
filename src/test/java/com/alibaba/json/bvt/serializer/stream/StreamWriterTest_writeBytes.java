package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;


public class StreamWriterTest_writeBytes extends TestCase {
    public void test_0() throws Exception {
        StringWriter out = new StringWriter();
        
        byte[] bytes = "民主".getBytes("GB2312");
        SerializeWriter writer = new SerializeWriter(out, 10);
        Assert.assertEquals(10, writer.getBufferLength());
        
        writer.writeByteArray(bytes);
        writer.close();
        
        String text = out.toString();
        byte[] result = JSON.parseObject(text, byte[].class);
        Assert.assertEquals("民主", new String(result, "GB2312"));
    }
}
