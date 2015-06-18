package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;


public class StreamWriterTest_writeChar extends TestCase {
    public void test_0() throws Exception {
        StringWriter out = new StringWriter();
        
        SerializeWriter writer = new SerializeWriter(out, 10);
        Assert.assertEquals(10, writer.getBufferLength());
        
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            writer.write(ch);
        }
        writer.close();
        
        String text = out.toString();
        Assert.assertEquals(26, text.length());
        
        for (int i = 0; i < 26; ++i) {
            Assert.assertEquals(text.charAt(i), (char)('a' + i));
        }
    }
}
