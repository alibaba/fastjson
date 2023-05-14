package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

/**
 * 测试字符':'的处理
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_colon extends TestCase {

    public void test_0() throws Exception {
        JSONScanner lexer = new JSONScanner(":true");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_1() throws Exception {
        JSONScanner lexer = new JSONScanner(" : true");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_2() throws Exception {
        JSONScanner lexer = new JSONScanner("\n:\ntrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_3() throws Exception {
        JSONScanner lexer = new JSONScanner("\r:\rtrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_4() throws Exception {
        JSONScanner lexer = new JSONScanner("\t:\ttrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_5() throws Exception {
        JSONScanner lexer = new JSONScanner("\t:\ftrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_6() throws Exception {
        JSONScanner lexer = new JSONScanner("\b:\btrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_f() throws Exception {
        JSONScanner lexer = new JSONScanner("\f:\btrue");
        lexer.nextTokenWithColon();
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_7() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("true");
            lexer.nextTokenWithColon();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_8() throws Exception {
        JSONScanner lexer = new JSONScanner("\b:\btrue");
        lexer.nextToken();
        Assert.assertEquals(JSONToken.COLON, lexer.token());
    }
}
