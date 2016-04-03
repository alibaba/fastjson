package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

/**
 * 测试字符':'的处理
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_colon extends TestCase {

    public void test_0() throws Exception {
        JSONLexer lexer = new JSONLexer(":true");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_1() throws Exception {
        JSONLexer lexer = new JSONLexer(" : true");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_2() throws Exception {
        JSONLexer lexer = new JSONLexer("\n:\ntrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_3() throws Exception {
        JSONLexer lexer = new JSONLexer("\r:\rtrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_4() throws Exception {
        JSONLexer lexer = new JSONLexer("\t:\ttrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_5() throws Exception {
        JSONLexer lexer = new JSONLexer("\t:\ftrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_6() throws Exception {
        JSONLexer lexer = new JSONLexer("\b:\btrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_f() throws Exception {
        JSONLexer lexer = new JSONLexer("\f:\btrue");
        lexer.nextTokenWithChar(':');
        Assert.assertEquals(JSONToken.TRUE, lexer.token());
    }

    public void test_7() throws Exception {
        JSONException error = null;
        try {
            JSONLexer lexer = new JSONLexer("true");
            lexer.nextTokenWithChar(':');
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_8() throws Exception {
        JSONLexer lexer = new JSONLexer("\b:\btrue");
        lexer.nextToken();
        Assert.assertEquals(JSONToken.COLON, lexer.token());
    }
}
