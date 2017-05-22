package com.alibaba.json.bvt.parser;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

public class DefaultExtJSONParserTest_7 extends TestCase {

    public void test_parse() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("1");
        Assert.assertEquals(Integer.valueOf(1), parser.parse());

        Exception error = null;
        try {
            parser.parse();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_parse_str() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("\"1\"");
        parser.config(Feature.AllowISO8601DateFormat, true);
        Assert.assertEquals("1", parser.parse());

    }

    public void test_parseArray() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1]");
        parser.config(Feature.AllowArbitraryCommas, false);
        List<String> list = new ArrayList<String>();
        parser.parseArray(String.class, list);
        Assert.assertEquals(1, list.size());
    }

    public void test_parseArray_error() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("[1,2}");
        parser.config(Feature.AllowArbitraryCommas, false);
        List<String> list = new ArrayList<String>();

        Exception error = null;
        try {
            parser.parseArray(String.class, list);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
