package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_obj_3 extends TestCase {

    public void test_obj() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"id\":123}"));

        reader.startObject();
        Assert.assertEquals("id", reader.readString());
        Assert.assertEquals(Integer.valueOf(123), reader.readInteger());
        reader.endObject();

        reader.close();
    }

    public void test_obj_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"val\":{\"id\":123}}"));

        reader.startObject();

        Assert.assertEquals("val", reader.readString());

        reader.startObject();
        Assert.assertEquals("id", reader.readString());
        Assert.assertEquals(Integer.valueOf(123), reader.readInteger());
        reader.endObject();

        reader.endObject();

        reader.close();
    }
    
    public void test_obj_3() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"val\":{\"val\":{\"id\":123}}}"));

        reader.startObject();
        Assert.assertEquals("val", reader.readString());
        
        reader.startObject();
        Assert.assertEquals("val", reader.readString());

        reader.startObject();
        Assert.assertEquals("id", reader.readString());
        Assert.assertEquals(Long.valueOf(123), reader.readLong());
        reader.endObject();

        reader.endObject();
        reader.endObject();

        reader.close();
    }

}
