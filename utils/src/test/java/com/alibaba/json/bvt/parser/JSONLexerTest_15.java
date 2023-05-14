package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class JSONLexerTest_15 extends TestCase {

    public void test_e() throws Exception {
        Assert.assertTrue(123e2D == ((Double) JSON.parse("123e2D")).doubleValue());
    }

}
