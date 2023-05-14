package com.alibaba.json.bvt.parser.stream;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONReaderScanner;

public class JSONReaderScannerTest_negative extends TestCase {

    public void test_double() throws Exception {
        char[] chars = "{\"value\":-3.5D}".toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner(chars, chars.length));
        JSONObject json = parser.parseObject();
        Assert.assertTrue(-3.5D == ((Double) json.get("value")).doubleValue());
        parser.close();
    }

    public void test_float() throws Exception {
        char[] chars = "{\"value\":-3.5F}".toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner(chars, chars.length));
        JSONObject json = parser.parseObject();
        Assert.assertTrue(-3.5F == ((Float) json.get("value")).doubleValue());
        parser.close();
    }

    public void test_decimal() throws Exception {
        char[] chars = "{\"value\":-3.5}".toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner(chars, chars.length));
        JSONObject json = parser.parseObject();
        Assert.assertEquals(new BigDecimal("-3.5"), json.get("value"));
        parser.close();
    }
    
    public void test_long() throws Exception {
        char[] chars = "{\"value\":-3L}".toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(new JSONReaderScanner(chars, chars.length));
        JSONObject json = parser.parseObject();
        Assert.assertTrue(-3L == ((Long) json.get("value")).longValue());
        parser.close();
    }
}
