package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;
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
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
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
            
            Assert.assertNotNull(key);
            Assert.assertNotNull(value);
            
            count++;
        }
        Assert.assertEquals(10, count);

        reader.endObject();
        reader.close();
    }

    public void test_read_2() throws Exception {
        JSONReader reader = new JSONReader(new JSONScanner("{{}:{},{}:{}}"));
        reader.startObject();

        Assert.assertTrue(reader.hasNext());
        reader.startObject();
        reader.endObject();

        reader.startObject();
        reader.endObject();

        Assert.assertTrue(reader.hasNext());
        reader.startObject();
        reader.endObject();

        reader.startObject();
        reader.endObject();
        
        Assert.assertFalse(reader.hasNext());

        reader.endObject();

        Exception error = null;
        try {
            reader.hasNext();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);

        reader.close();
    }
}
