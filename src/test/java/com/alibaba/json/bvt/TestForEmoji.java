package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import junit.framework.TestCase;


public class TestForEmoji extends TestCase {
    public void test_0 () throws Exception {
        Assert.assertEquals("\"\uE507\"", JSON.toJSONString("\uE507"));
        Assert.assertEquals("\"\uE501\"", JSON.toJSONString("\uE501"));
        Assert.assertEquals("\"\uE44C\"", JSON.toJSONString("\uE44C"));
        Assert.assertEquals("\"\uE401\"", JSON.toJSONString("\uE401"));
        Assert.assertEquals("\"\uE253\"", JSON.toJSONString("\uE253"));
        Assert.assertEquals("\"\uE201\"", JSON.toJSONString("\uE201"));
        Assert.assertEquals("\"\uE15A\"", JSON.toJSONString("\uE15A"));
        Assert.assertEquals("\"\uE101\"", JSON.toJSONString("\uE101"));
        Assert.assertEquals("\"\uE05A\"", JSON.toJSONString("\uE05A"));
        Assert.assertEquals("\"\uE001\"", JSON.toJSONString("\uE001"));
        //E507
    }
    
    public void test_zh() throws Exception {
    	Assert.assertEquals("\"\u4E2D\u56FD\"", JSON.toJSONString("中国"));
    }
}
