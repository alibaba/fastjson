package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_error extends TestCase {

    public void test_0() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[]"));

        Exception error = null;
        try {
            reader.hasNext();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
        
    }

    public void test_1() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("{\"id\":123}"));

        reader.startObject();
        reader.readObject();

        Exception error = null;
        try {
            reader.hasNext();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
