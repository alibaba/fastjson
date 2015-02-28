package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONScanner;

public class Issue119 extends TestCase {

    public void test_for_issue() throws Exception {
        JSONScanner lexer = new JSONScanner("-100S");
        lexer.resetStringPosition();
        lexer.scanNumber();
        Assert.assertEquals(Short.class, lexer.integerValue().getClass());
        Assert.assertEquals(-100, lexer.integerValue().shortValue());
        lexer.close();
    }
}
