package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_string extends TestCase {

    public void test_array() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[\"abc\"]"));

        reader.startArray();

        Assert.assertEquals("abc", reader.readString());

        reader.endArray();

        reader.close();
    }
    
    public void test_array_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[[\"abc\"]]"));

        reader.startArray();
        reader.startArray();

        Assert.assertEquals("abc", reader.readString());

        reader.endArray();
        reader.endArray();

        reader.close();
    }
    
    public void test_array_3() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[[[\"abc\"]]]"));

        reader.startArray();
        reader.startArray();
        reader.startArray();

        Assert.assertEquals("abc", reader.readString());

        reader.endArray();
        reader.endArray();
        reader.endArray();

        reader.close();
    }

}
