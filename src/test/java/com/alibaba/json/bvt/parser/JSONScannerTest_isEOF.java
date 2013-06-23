package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest_isEOF extends TestCase {

    public void test_0() throws Exception {
        String text = "{}  ";
        JSONObject obj = JSON.parseObject(text);
        Assert.assertEquals(0, obj.size());
    }

    public void test_1() throws Exception {
        JSONScanner lexer = new JSONScanner("  ");
        lexer.nextToken();
        Assert.assertTrue(lexer.token() == JSONToken.EOF);
    }

    public void test_2() throws Exception {
        JSONScanner lexer = new JSONScanner("1  ");
        lexer.nextToken();
        lexer.nextToken();
        Assert.assertTrue(lexer.token() == JSONToken.EOF);
    }

    public void test_3() throws Exception {
        JSONScanner lexer = new JSONScanner(" {}");
        lexer.nextToken();
        Assert.assertTrue(lexer.token() != JSONToken.EOF);
    }

}
