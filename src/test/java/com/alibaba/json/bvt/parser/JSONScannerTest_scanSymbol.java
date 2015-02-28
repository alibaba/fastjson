package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.SymbolTable;

/**
 * 测试字符':'的处理
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_scanSymbol extends TestCase {

    public void test_0() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\\n\"");
        String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
        Assert.assertNull(text);
        Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }

    public void test_1() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"},");
        String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
        Assert.assertEquals("aa", text);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.COMMA, lexer.token());
    }
    
    public void test_2() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}]");
        String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
        Assert.assertEquals("aa", text);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.RBRACKET, lexer.token());
    }
    
    public void test_3() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}}");
        String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
        Assert.assertEquals("aa", text);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.RBRACE, lexer.token());
    }
    
    public void test_4() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}");
    	String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
    	Assert.assertEquals("aa", text);
    	Assert.assertEquals(JSONScanner.END, lexer.matchStat());
    	Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    
    public void test_6() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}{");
    	String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
    	Assert.assertEquals(null, text);
    	Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }
    
    public void test_7() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"");
    	String text = lexer.scanFieldSymbol("\"value\":".toCharArray(), new SymbolTable());
    	Assert.assertEquals(null, text);
    	Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }
}
