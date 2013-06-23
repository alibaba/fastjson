package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public class JSONScannerTest_singQuoteString extends TestCase {

    public void test_string() throws Exception {
        {
            JSONScanner lexer = new JSONScanner("\'中国\'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals("中国", lexer.stringVal());
        }
        {
            JSONScanner lexer = new JSONScanner("'中国\t\\'\\\"'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals("中国\t'\"", lexer.stringVal());
        }
        {
            JSONScanner lexer = new JSONScanner("\'中国\tV5\'");
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

        JSONScanner lexer = new JSONScanner(text.toCharArray(), text.length() - 1);
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, lexer.pos());

        String stringVal = lexer.stringVal();

        Assert.assertEquals("\"\\\\/\\b\\f\\n\\r\\t中\"", JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }

    public void test_string2() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('\'');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\\\\\/\\b\\f\\n\\r\\t\\u" + Integer.toHexString('中'));
        }
        buf.append('\'');

        String text = buf.toString();

        JSONScanner lexer = new JSONScanner(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, lexer.pos());

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

        JSONScanner lexer = new JSONScanner(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, lexer.pos());

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

        JSONScanner lexer = new JSONScanner(text.toCharArray(), text.length());
        lexer.config(Feature.AllowSingleQuotes, true);
        lexer.nextToken();

        Assert.assertEquals(0, lexer.pos());

        String stringVal = lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

        JSON.toJSONString(stringVal);
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSONScanner lexer = new JSONScanner("'k");
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
            JSONScanner lexer = new JSONScanner("'k\\k'");
            lexer.config(Feature.AllowSingleQuotes, true);
            lexer.nextToken();
            Assert.assertEquals(JSONToken.ERROR, lexer.token());
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
