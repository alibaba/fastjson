package com.alibaba.json.bvt.serializer;

import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_1 extends TestCase {
    public void test_0 () throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.UseSingleQuotes);
        out.writeString("abc");
        Assert.assertEquals("'abc'", out.toString());
    }
    
    public void test_1 () throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.UseSingleQuotes);
        out.writeString("abc中文");
        
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        out.writeTo(byteOut, "UTF-8");
        Assert.assertEquals("'abc中文'", new String(byteOut.toByteArray(), "UTF-8"));
    }
    
    public void test_2 () throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.UseSingleQuotes);
        out.writeString("abc");
        Assert.assertEquals("'abc'", new String(out.toBytes((String) null), "ISO-8859-1"));
    }
    
    public void test_3 () throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.UseSingleQuotes);
        out.writeString("abc");
        Assert.assertEquals("'abc'", new String(out.toBytes("UTF-16"), "UTF-16"));
    }
    
    public void test_5 () throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.write((String) null);
        Assert.assertEquals("null", new String(out.toBytes("UTF-16"), "UTF-16"));
    }
    
    public void test_6 () throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeString("中文");
        Assert.assertEquals("\"中文\"", new String(out.toBytes("UTF-16"), "UTF-16"));
    }
    
    public void test_null () throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeString((String) null);
        Assert.assertEquals("null", new String(out.toBytes("UTF-16"), "UTF-16"));
    }
}
