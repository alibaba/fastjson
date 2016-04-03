package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

import junit.framework.TestCase;

public class JSONLexerTest_8 extends TestCase {

    public void test_ident() throws Exception {
        JSONLexer lexer = new JSONLexer("123");
        lexer.nextIdent();
        org.junit.Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.close();
    }

    public void test_ident_2() throws Exception {
        JSONLexer lexer = new JSONLexer("\uFEFF123");
        lexer.nextIdent();
        org.junit.Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.close();
    }
    
}
