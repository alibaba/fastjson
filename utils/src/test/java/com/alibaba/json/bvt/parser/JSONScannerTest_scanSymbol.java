package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.util.TypeUtils;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.SymbolTable;

import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_hashcode;
import static com.alibaba.fastjson.util.TypeUtils.fnv1a_64_magic_prime;

/**
 * 测试字符':'的处理
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_scanSymbol extends TestCase {

    public void test_0() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\\n\"");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        assertEquals(0, hashCode);
        Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }

    public void test_1() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"},");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        Assert.assertEquals(fnv_hash("aa"), hashCode);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.COMMA, lexer.token());
    }
    
    public void test_2() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}]");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        Assert.assertEquals(fnv_hash("aa"), hashCode);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.RBRACKET, lexer.token());
    }
    
    public void test_3() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}}");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        Assert.assertEquals(fnv_hash("aa"), hashCode);
        Assert.assertEquals(JSONScanner.END, lexer.matchStat());
        Assert.assertEquals(JSONToken.RBRACE, lexer.token());
    }
    
    public void test_4() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
    	Assert.assertEquals(fnv_hash("aa"), hashCode);
    	Assert.assertEquals(JSONScanner.END, lexer.matchStat());
    	Assert.assertEquals(JSONToken.EOF, lexer.token());
    }
    
    public void test_6() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"}{");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
    	Assert.assertEquals(0, hashCode);
    	Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }
    
    public void test_7() throws Exception {
    	JSONScanner lexer = new JSONScanner("\"value\":\"aa\"");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
    	Assert.assertEquals(0, hashCode);
    	Assert.assertEquals(JSONScanner.NOT_MATCH, lexer.matchStat());
    }

    public void test_8() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\": \"MINUTES\",");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        assertEquals(189130438399835214L, hashCode);
        Assert.assertEquals(JSONScanner.VALUE, lexer.matchStat());
    }

    public void test_9() throws Exception {
        JSONScanner lexer = new JSONScanner("\"value\":\"MINUTES\",");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        assertEquals(189130438399835214L, hashCode);
        Assert.assertEquals(JSONScanner.VALUE, lexer.matchStat());
    }

    public void test_10() throws Exception {
        JSONScanner lexer = new JSONScanner("      \"value\":\"MINUTES\",");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        assertEquals(189130438399835214L, hashCode);
        Assert.assertEquals(JSONScanner.VALUE, lexer.matchStat());
    }

    public void test_11() throws Exception {
        JSONScanner lexer = new JSONScanner("      \"value\":\"A\",");
        long hashCode = lexer.scanFieldSymbol("\"value\":".toCharArray());
        assertEquals(TypeUtils.fnv1a_64("A"), hashCode);
        Assert.assertEquals(JSONScanner.VALUE, lexer.matchStat());
    }

    static long fnv_hash(String text) {
        long hash = fnv1a_64_magic_hashcode;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            hash ^= c;
            hash *= fnv1a_64_magic_prime;
        }
        return hash;
    }
}
