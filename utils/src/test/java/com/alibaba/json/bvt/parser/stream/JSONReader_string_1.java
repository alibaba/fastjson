package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReader_string_1 extends TestCase {
    public void test_obj() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("\"abc\""));

        Assert.assertEquals("abc", reader.readString());

        reader.close();
    }

}
