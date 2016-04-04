package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.SerializeWriterTestUtils;

import junit.framework.TestCase;


public class StreamWriterTest_writeFieldValue_string_singQuote extends TestCase {
    public void test_0() throws Exception {
        StringWriter out = new StringWriter();
        
        SerializeWriter writer = new SerializeWriter(out, 10);
        writer.config(SerializerFeature.QuoteFieldNames, true);
        writer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(10, SerializeWriterTestUtils.getBufferLength(writer));
        
        writer.write(',');
        writer.writeFieldName("abcde01245abcde", true);
        writer.writeString("123");
        writer.close();
        
        String text = out.toString();
        Assert.assertEquals(",'abcde01245abcde':'123'", text);
    }
}
