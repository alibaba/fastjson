package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

import junit.framework.TestCase;

public class JSONScannerTest_ident extends TestCase {
    
    Method method;
    
    public JSONScannerTest_ident() {
        
    }
    
    protected void setUp() throws Exception {
        method = JSONLexer.class.getDeclaredMethod("scanIdent");
        method.setAccessible(true);
    }

    public void test_true() throws Exception {
        JSONLexer lexer = new JSONLexer("true");
        method.invoke(lexer);
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_false() throws Exception {
        JSONLexer lexer = new JSONLexer("false");
        method.invoke(lexer);
        Assert.assertEquals(JSONToken.FALSE, lexer.token());
    }

    public void test_null() throws Exception {
        JSONLexer lexer = new JSONLexer("null");
        method.invoke(lexer);
        Assert.assertEquals(JSONToken.NULL, lexer.token());
    }

    public void test_new() throws Exception {
        JSONLexer lexer = new JSONLexer("new");
        method.invoke(lexer);
        Assert.assertEquals(JSONToken.NEW, lexer.token());
    }

    public void test_Date() throws Exception {
        String text = "Date";
        JSONLexer lexer = new JSONLexer(text);
        method.invoke(lexer);
        Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        Assert.assertEquals(text, lexer.stringVal());
    }
}
