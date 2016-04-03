package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.SerializeWriterTestUtils;


public class StreamWriterTest_writeValueString2 extends TestCase {
    public void test_0() throws Exception {
        StringWriter out = new StringWriter();
        
        SerializeWriter writer = new SerializeWriter(out, 10);
        Assert.assertEquals(10, SerializeWriterTestUtils.getBufferLength(writer));
        
        writer.writeString("abcde12345678\"\\");
        writer.close();
        
        String text = out.toString();
        Assert.assertEquals("\"abcde12345678\\\"\\\\\"", text);
    }
}
