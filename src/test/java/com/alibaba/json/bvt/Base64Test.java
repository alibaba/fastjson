package com.alibaba.json.bvt;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;


public class Base64Test extends TestCase {
    public void test_base64() throws Exception {
        Assert.assertEquals(JSONLexer.decodeFast("", 0, 0).length, 0);
        Assert.assertEquals(JSONLexer.decodeFast("ABC", 0, 3).length, 2);
    }
}
