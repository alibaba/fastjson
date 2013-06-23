package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;

public class FeatureParserTest extends TestCase {

    public void test_AllowSingleQuotes_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{'a':3}");
        parser.config(Feature.AllowSingleQuotes, true);
        JSONObject json = (JSONObject) parser.parse();
        Assert.assertEquals(1, json.size());
        Assert.assertEquals(new Integer(3), (Integer) json.getInteger("a"));
    }

    public void test_AllowSingleQuotes_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{'a':'3'}");
        parser.config(Feature.AllowSingleQuotes, true);
        JSONObject json = (JSONObject) parser.parse();
        Assert.assertEquals(1, json.size());
        Assert.assertEquals("3", (String) json.get("a"));
    }

    public void test_AllowUnQuotedFieldNames_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
        parser.config(Feature.AllowUnQuotedFieldNames, true);
        JSONObject json = (JSONObject) parser.parse();
        Assert.assertEquals(1, json.size());
        Assert.assertEquals(new Integer(3), (Integer) json.getInteger("a"));
    }

    public void test_error_0() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{'a':3}");
            parser.config(Feature.AllowSingleQuotes, false);
            parser.parse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{\"a\":'3'}");
            parser.config(Feature.AllowSingleQuotes, false);
            parser.parse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("{a:3}");
            parser.config(Feature.AllowUnQuotedFieldNames, false);
            parser.parse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }
}
