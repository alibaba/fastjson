package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class JSONLexerTest extends TestCase {
    public void test_0 () throws Exception {
        StringBuilder buf = new StringBuilder("{\"value\":\"");
        for (int i = 0; i < 256; ++i) {
            buf.append('a');
        }
        buf.append("\\n");
        buf.append("\"}");
        
        JSONObject json = JSON.parseObject(buf.toString());
        Assert.assertEquals(257, json.getString("value").length());
        Assert.assertEquals('a', json.getString("value").charAt(255));
        Assert.assertEquals('\n', json.getString("value").charAt(256));
    }
}
