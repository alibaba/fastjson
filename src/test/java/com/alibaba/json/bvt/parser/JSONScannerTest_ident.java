package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest_ident extends TestCase {

    public void test_true() throws Exception {
        JSONScanner lexer = new JSONScanner("true");
        lexer.scanIdent();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_false() throws Exception {
        JSONScanner lexer = new JSONScanner("false");
        lexer.scanIdent();
        Assert.assertEquals(JSONToken.FALSE, lexer.token());
    }

    public void test_null() throws Exception {
        JSONScanner lexer = new JSONScanner("null");
        lexer.scanIdent();
        Assert.assertEquals(JSONToken.NULL, lexer.token());
    }

    public void test_new() throws Exception {
        JSONScanner lexer = new JSONScanner("new");
        lexer.scanIdent();
        Assert.assertEquals(JSONToken.NEW, lexer.token());
    }

    public void test_Date() throws Exception {
        String text = "Date";
        JSONScanner lexer = new JSONScanner(text);
        lexer.scanIdent();
        Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        Assert.assertEquals(text, lexer.stringVal());
    }
}
