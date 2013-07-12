package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_16 extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\0\":123}").get("\0"));
    }

    public void test_1() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\1\":123}").get("\1"));
    }
    
    public void test_2() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\2\":123}").get("\2"));
    }
    
    public void test_3() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\3\":123}").get("\3"));
    }
    
    public void test_4() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\4\":123}").get("\4"));
    }
    
    public void test_5() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\5\":123}").get("\5"));
    }
    
    public void test_6() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\6\":123}").get("\6"));
    }
    
    public void test_7() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\7\":123}").get("\7"));
    }
    
    public void test_8() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\b\":123}").get("\b"));
    }
    
    public void test_9() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\t\":123}").get("\t"));
    }
    
    public void test_10() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\n\":123}").get("\n"));
    }
    
    public void test_39() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\'\":123}").get("\'"));
    }
    
    public void test_40() throws Exception {
        Assert.assertEquals(123, JSON.parseObject("{\"\\xFF\":123}").get("\u00FF"));
    }
}
