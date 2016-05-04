package com.alibaba.json.bvt.stream;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONWriterTest_2 extends TestCase {

    public void test_writer() throws Exception {
        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);
        writer.config(SerializerFeature.UseSingleQuotes, true);
        writer.startObject();
        writer.writeObject("a");
        writer.writeObject("1");
        
        writer.writeObject("b");
        writer.writeObject("2");
        
        writer.writeObject("c");
        writer.writeObject("3");
        
        writer.endObject();
        writer.close();

        Assert.assertEquals("{'a':'1','b':'2','c':'3'}", out.toString());
    }
}
