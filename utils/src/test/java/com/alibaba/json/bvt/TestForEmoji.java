package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import junit.framework.TestCase;


public class TestForEmoji extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertEquals("\"\\uE507\"", JSON.toJSONString("\uE507", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE501\"", JSON.toJSONString("\uE501", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE44C\"", JSON.toJSONString("\uE44C", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE401\"", JSON.toJSONString("\uE401", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE253\"", JSON.toJSONString("\uE253", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE201\"", JSON.toJSONString("\uE201", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE15A\"", JSON.toJSONString("\uE15A", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE101\"", JSON.toJSONString("\uE101", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE05A\"", JSON.toJSONString("\uE05A", SerializerFeature.BrowserCompatible));
        Assert.assertEquals("\"\\uE001\"", JSON.toJSONString("\uE001", SerializerFeature.BrowserCompatible));
        //E507
    }
    
    public void test_zh() throws Exception {
    	Assert.assertEquals("\"\\u4E2D\\u56FD\"", JSON.toJSONString("中国", SerializerFeature.BrowserCompatible));
    }
}
