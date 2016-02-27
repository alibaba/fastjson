package com.alibaba.json.bvt.parser;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class DateParserTest extends TestCase {

    public void test_date_new() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("new Date(1294552193254)");

        java.util.Date date = parser.parseObject(java.util.Date.class);

        Assert.assertEquals(new java.util.Date(1294552193254L), date);
        parser.close();
    }
    
    public void test_date_new_1() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("new Date(1294552193254)");

        java.util.Date date = (java.util.Date) parser.parse();

        Assert.assertEquals(new java.util.Date(1294552193254L), date);
        parser.close();
    }
    
    public void test_date_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("1294552193254");

        java.util.Date date = parser.parseObject(java.util.Date.class);

        Assert.assertEquals(new java.util.Date(1294552193254L), date);
        parser.close();
    }

    public void test_date_1() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09T13:49:53.254\"", ParserConfig.getGlobalInstance(), features);

        java.util.Date date = parser.parseObject(java.util.Date.class);

        Assert.assertEquals(new java.util.Date(1294552193254L), date);
        parser.close();
    }

    public void test_date_2() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        DefaultJSONParser parser = new DefaultJSONParser("new Date(1294552193254)", ParserConfig.getGlobalInstance(), features);

        java.util.Date date = parser.parseObject(java.util.Date.class);

        Assert.assertEquals(new java.util.Date(1294552193254L), date);
        parser.close();
    }

    public void test_date_3() throws Exception {
        java.util.Date date = JSON.parseObject("\"2011-01-09T13:49:53\"", java.util.Date.class, Feature.AllowISO8601DateFormat);

        Assert.assertEquals(new java.util.Date(1294552193000L), date);
    }

    public void test_date_4() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09\"", ParserConfig.getGlobalInstance(), features);

        java.util.Date date = parser.parseObject(java.util.Date.class);

        Assert.assertEquals(new java.util.Date(1294502400000L), date);
        parser.close();
    }

    public void test_date_5() throws Exception {
        JSONObject object = JSON.parseObject("{d:'2011-01-09T13:49:53'}", Feature.AllowISO8601DateFormat);
        Assert.assertEquals(new java.util.Date(1294552193000L), object.get("d"));
    }

    public void test_date_6() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);

        java.util.Date date = JSON.parseObject("{d:\"2011-01-09T13:49:53\"}", Entity.class, Feature.AllowISO8601DateFormat).getD();

        Assert.assertEquals(new java.util.Date(1294552193000L), date);
    }

    public void test_date_7() throws Exception {
        Entity entity = JSON.parseObject("{d:'2011-01-09T13:49:53'}", Entity.class, Feature.AllowISO8601DateFormat);
        java.util.Date date = entity.getD();

        Assert.assertEquals(new java.util.Date(1294552193000L), date);
    }

    public void test_date_error_0() throws Exception {

        JSONException error = null;
        try {
            DefaultJSONParser parser = new DefaultJSONParser("true");

            parser.parseObject(java.util.Date.class);
            parser.close();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class Entity {

        private Date d;

        public Date getD() {
            return d;
        }

        public void setD(Date d) {
            this.d = d;
        }

    }
}
