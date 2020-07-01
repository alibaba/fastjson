package com.alibaba.json.bvt.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class DefaultJSONParserTest_date extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_date() {
        String text = "{\"date\":\"2011-01-09T13:49:53.254\"}";
        char[] chars = text.toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(chars, chars.length, ParserConfig.getGlobalInstance(), 0);
        parser.config(Feature.AllowISO8601DateFormat, true);
        JSONObject json = parser.parseObject();
        Assert.assertEquals(new Date(1294552193254L), json.get("date"));
    }
    
    
    public void test_date2() {
        String text = "{\"date\":\"xxxxx\"}";
        char[] chars = text.toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(chars, chars.length, ParserConfig.getGlobalInstance(), 0);
        parser.config(Feature.AllowISO8601DateFormat, true);
        JSONObject json = parser.parseObject();
        Assert.assertEquals("xxxxx", json.get("date"));
    }
    
    public void test_date3() {
        String text = "{\"1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst\\t\":\"xxxxx\"}";
        char[] chars = text.toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(chars, chars.length, ParserConfig.getGlobalInstance(), 0);
        parser.config(Feature.AllowISO8601DateFormat, true);
        JSONObject json = parser.parseObject();
        Assert.assertEquals("xxxxx", json.get("1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst\t"));
    }
    
    public void test_date4() {
        String text = "{\"1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst\\t\":\"xxxxx\"}";
        char[] chars = text.toCharArray();
        DefaultJSONParser parser = new DefaultJSONParser(chars, chars.length, ParserConfig.getGlobalInstance(), 0);
        parser.config(Feature.AllowISO8601DateFormat, true);
        JSONObject json = parser.parseObject();
        Assert.assertEquals("xxxxx", json.get("1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst1234567890abcdefghijklmnopqrst\t"));
    }
    
    public void test_dateFormat() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("{}");
        parser.setDateFormat("yyyy-DD-mm");
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-DD-mm", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        
        parser.setDateFomrat(format);
        parser.getDateFomartPattern();
        parser.getDateFormat();
        parser.parse();
        parser.close();
    }
}
