package com.alibaba.json.bvt.parser;

import java.lang.reflect.Field;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest_singQuoteString extends TestCase {

    public void test_string() throws Exception {
        {
            JSONLexer lexer = new JSONLexer("\'中国\'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals("中国", lexer.stringVal());
        }
        {
            JSONLexer lexer = new JSONLexer("'中国\t\\'\\\"'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals("中国\t'\"", lexer.stringVal());
        }
        {
            JSONLexer lexer = new JSONLexer("\'中国\tV5\'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals("中国\tV5", lexer.stringVal());
        }

        StringBuilder buf = new StringBuilder();

        buf.append('\'');
        buf.append("\\\\\\/\\b\\f\\n\\r\t\\u" + Integer.toHexString('中'));
        buf.append('\'');
        buf.append('\u2001');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length() - 1);
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        String stringVal = lexer.stringVal();

        Assert.assertEquals("\"\\\\/\\b\\f\\n\\r\\t中\"", JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }
    
    int pos(JSONLexer lexer) throws Exception {
        Field field = JSONLexer.class.getDeclaredField("pos");
        field.setAccessible(true);
        return field.getInt(lexer);
    }

    public void test_string2() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('\'');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\\\\\/\\b\\f\\n\\r\\t\\u" + Integer.toHexString('中'));
        }
        buf.append('\'');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        String stringVal = lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }

    public void test_string3() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('\'');
        for (int i = 0; i < 200; ++i) {
            buf.append("abcdefghijklmn012345689ABCDEFG");
        }
        buf.append('\'');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        String stringVal = lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }

    public void test_string4() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('\'');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\tabcdefghijklmn012345689ABCDEFG");
        }
        buf.append('\'');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        String stringVal = lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("'k");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("'k\\k'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            lexer.stringVal();
            Assert.assertEquals(JSONToken.ERROR, lexer.token());
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
