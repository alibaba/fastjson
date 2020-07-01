package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest__nextToken extends TestCase {
    public void test_next() throws Exception {
        String text = "\"aaa\"";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LITERAL_INT);
        Assert.assertEquals(JSONToken.LITERAL_STRING, lexer.token());
    }
    
    public void test_next_1() throws Exception {
        String text = "[";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LITERAL_INT);
        Assert.assertEquals(JSONToken.LBRACKET, lexer.token());
    }
    
    public void test_next_2() throws Exception {
        String text = "{";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LITERAL_INT);
        Assert.assertEquals(JSONToken.LBRACE, lexer.token());
    }
    
    public void test_next_3() throws Exception {
        String text = "{";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.LBRACE, lexer.token());
    }
    
    public void test_next_4() throws Exception {
        String text = "";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    
    public void test_next_5() throws Exception {
        String text = " \n\r\t\f\b 1";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.LITERAL_INT, lexer.token());
    }
    
    public void test_next_6() throws Exception {
        String text = "";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.EOF);
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    
    public void test_next_7() throws Exception {
        String text = "{";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextToken(JSONToken.EOF);
        Assert.assertEquals(JSONToken.LBRACE, lexer.token());
    }
    
    public void test_next_8() throws Exception {
        String text = "\n\r\t\f\b :{";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACE);
        Assert.assertEquals(JSONToken.LBRACE, lexer.token());
    }
    
    public void test_next_9() throws Exception {
        String text = "\n\r\t\f\b :[";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACE);
        Assert.assertEquals(JSONToken.LBRACKET, lexer.token());
    }
    
    public void test_next_10() throws Exception {
        String text = "\n\r\t\f\b :";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACE);
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    
    public void test_next_11() throws Exception {
        String text = "\n\r\t\f\b :{";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.LBRACE, lexer.token());
    }
    
    public void test_next_12() throws Exception {
        String text = "\n\r\t\f\b :";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    public void test_next_13() throws Exception {
        String text = "\n\r\t\f\b :\n\r\t\f\b ";
        JSONScanner lexer = new JSONScanner(text);
        lexer.nextTokenWithColon(JSONToken.LBRACKET);
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
}
