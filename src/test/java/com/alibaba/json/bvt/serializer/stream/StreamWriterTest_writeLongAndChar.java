package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;


public class StreamWriterTest_writeLongAndChar extends TestCase {
    public void test_0() throws Exception {
        StringWriter out = new StringWriter();
        
        SerializeWriter writer = new SerializeWriter(out, 10);
        Assert.assertEquals(10, writer.getBufferLength());
        
        writer.write("abcde");
        writer.writeLong(12345678L);
        writer.write(',');
        writer.close();
        
        String text = out.toString();
        Assert.assertEquals("abcde12345678,", text);
    }
}
