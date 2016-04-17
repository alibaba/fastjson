package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class Issue119 extends TestCase {

    public void test_for_issue() throws Exception {
        JSONLexer lexer = new JSONLexer("-100S");
        lexer.scanNumber();
        Assert.assertEquals(Short.class, lexer.integerValue().getClass());
        Assert.assertEquals(-100, lexer.integerValue().shortValue());
        lexer.close();
    }
}
