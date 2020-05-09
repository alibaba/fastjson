package com.alibaba.json.bvt.parser;

import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

@SuppressWarnings("deprecation")
public class DateParserTest_sql_timestamp extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void f_test_date_0() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("1294552193254");

        java.sql.Timestamp date = parser.parseObject(java.sql.Timestamp.class);

        Assert.assertEquals(new java.sql.Timestamp(1294552193254L), date);
        parser.close();
    }

    public void test_date_1() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09T13:49:53.254\"", ParserConfig.getGlobalInstance(), features);

        java.sql.Timestamp date = parser.parseObject(java.sql.Timestamp.class);

        Assert.assertEquals(new java.sql.Timestamp(1294552193254L), date);
        parser.close();
    }

    public void test_date_2() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        DefaultJSONParser parser = new DefaultJSONParser("new Date(1294552193254)", ParserConfig.getGlobalInstance(), features);

        java.sql.Timestamp date = parser.parseObject(java.sql.Timestamp.class);

        Assert.assertEquals(new java.sql.Timestamp(1294552193254L), date);
        parser.close();
    }

    public void test_date_3() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09T13:49:53\"", ParserConfig.getGlobalInstance(), features);

        java.sql.Timestamp date = parser.parseObject(java.sql.Timestamp.class);

        Assert.assertEquals(new java.sql.Timestamp(1294552193000L), date);
        parser.close();
    }

    public void test_date_4() throws Exception {
        int features = JSON.DEFAULT_PARSER_FEATURE;
        features = Feature.config(features, Feature.AllowISO8601DateFormat, true);
        DefaultJSONParser parser = new DefaultJSONParser("\"2011-01-09\"", ParserConfig.getGlobalInstance(), features);

        java.sql.Timestamp date = parser.parseObject(java.sql.Timestamp.class);

        Assert.assertEquals(new java.sql.Timestamp(1294502400000L), date);
        parser.close();
    }
}
