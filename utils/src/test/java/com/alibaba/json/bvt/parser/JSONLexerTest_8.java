package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONLexerTest_8 extends TestCase {

    public void test_ident() throws Exception {
        JSONScanner lexer = new JSONScanner("123");
        lexer.nextIdent();
        org.junit.Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.close();
    }

    public void test_ident_2() throws Exception {
        JSONScanner lexer = new JSONScanner("\uFEFF123");
        lexer.nextIdent();
        org.junit.Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.close();
    }
    
    public void test_ident_3() throws Exception {
        JSONReaderScanner lexer = new JSONReaderScanner("\uFEFF123");
        lexer.nextIdent();
        org.junit.Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
        lexer.close();
    }
}
