package com.alibaba.json.bvt.parser;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_top extends TestCase {

    public void test_int() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("123"));

        Assert.assertEquals(new Integer(123), reader.readInteger());

        reader.close();
    }

    public void test_long() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("123"));

        Assert.assertEquals(new Long(123), reader.readLong());

        reader.close();
    }
}
