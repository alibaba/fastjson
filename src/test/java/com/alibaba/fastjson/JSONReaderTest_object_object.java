package com.alibaba.fastjson;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONScanner;

public class JSONReaderTest_object_object extends TestCase {

    String text = "{\"f0\":{},\"f1\":{},\"f2\":{},\"f3\":{},\"f4\":{}, " + //
                  "\"f5\":{},\"f6\":{},\"f7\":{},\"f8\":{},\"f9\":{}}";

    public void test_read() throws Exception {

        JSONReader reader = new JSONReader(new StringReader(text));
        reader.startObject();

        int count = 0;
        while (reader.hasNext()) {
            String key = (String) reader.readObject();
            Object value = reader.readObject();
            count++;
        }
        Assert.assertEquals(10, count);

        reader.endObject();
        reader.close();
    }

    public void test_read_1() throws Exception {
        JSONReader reader = new JSONReader(new JSONScanner(text));
        reader.startObject();

        int count = 0;
        while (reader.hasNext()) {
            String key = (String) reader.readObject();
            Object value = reader.readObject();
            count++;
        }
        Assert.assertEquals(10, count);

        reader.endObject();
        reader.close();
    }
}
