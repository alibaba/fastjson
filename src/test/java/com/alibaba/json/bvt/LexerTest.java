/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

import junit.framework.TestCase;

public class LexerTest extends TestCase {

    public void test_float() throws Exception {
        String text = "123456789.0123";
        JSONLexer lexer = new JSONLexer(text);
        lexer.nextToken();
        BigDecimal decimalValue = lexer.decimalValue();
        Assert.assertEquals(new BigDecimal("123456789.0123"), decimalValue);

    }

    public void test_string() throws Exception {
        {
            JSONLexer lexer = new JSONLexer("\"中国\"");
            lexer.nextToken();
            Assert.assertEquals("中国", lexer.stringVal());
        }
        {
            JSONLexer lexer = new JSONLexer("\"中国\t\"");
            lexer.nextToken();
            Assert.assertEquals("中国\t", lexer.stringVal());
        }
        {
            JSONLexer lexer = new JSONLexer("\"中国\tV5\"");
            lexer.nextToken();
            Assert.assertEquals("中国\tV5", lexer.stringVal());
        }

        StringBuilder buf = new StringBuilder();

        buf.append('"');
        buf.append("\\\\\\/\\b\\f\\n\\r\\t\\u" + Integer.toHexString('中'));
        buf.append('"');
        buf.append('\u2001');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length() - 1);
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        String stringVal = lexer.stringVal();

        Assert.assertEquals("\"\\\\/\\b\\f\\n\\r\\t中\"", JSON.toJSONString(stringVal));

    }
    
    int pos(JSONLexer lexer) throws Exception {
        Field field = JSONLexer.class.getDeclaredField("pos");
        field.setAccessible(true);
        return field.getInt(lexer);
    }

    public void test_string2() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('"');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\\\\\/\\b\\f\\n\\r\\t\\u" + Integer.toHexString('中'));
        }
        buf.append('"');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        lexer.stringVal();

    }

    public void test_string3() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('"');
        for (int i = 0; i < 200; ++i) {
            buf.append("abcdefghijklmn012345689ABCDEFG");
        }
        buf.append('"');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        lexer.stringVal();
    }

    public void test_string4() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('"');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\tabcdefghijklmn012345689ABCDEFG");
        }
        buf.append('"');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

    }

    public void test_empty() throws Exception {
        JSONLexer lexer = new JSONLexer("".toCharArray(), 0);
        lexer.nextToken();
        Assert.assertEquals(JSONToken.EOF, lexer.token());
    }

//    public void test_isWhitespace() throws Exception {
//        new JSONLexer("".toCharArray(), 0);
//        Assert.assertTrue(JSONLexer.isWhitespace(' '));
//        Assert.assertTrue(JSONLexer.isWhitespace('\b'));
//        Assert.assertTrue(JSONLexer.isWhitespace('\f'));
//        Assert.assertTrue(JSONLexer.isWhitespace('\n'));
//        Assert.assertTrue(JSONLexer.isWhitespace('\r'));
//        Assert.assertTrue(JSONLexer.isWhitespace('\t'));
//        Assert.assertFalse(JSONLexer.isWhitespace('k'));
//    }

    public void test_error() throws Exception {
        JSONLexer lexer = new JSONLexer("k");
        lexer.nextToken();
        Assert.assertEquals(JSONToken.ERROR, lexer.token());
    }

    public void test_error1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("\"\\k\"");
            lexer.nextToken();
            lexer.stringVal();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void f_test_ident() throws Exception {
        {
            JSONLexer lexer = new JSONLexer("ttue");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("tree");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("truu");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("fflse");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("nalse");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("faase");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("falle");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("falss");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("nnll");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("nuul");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            JSONLexer lexer = new JSONLexer("nulk");
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
        {
            StringBuilder buf = new StringBuilder();
            buf.append('n');
            for (char ch = 'A'; ch <= 'Z'; ++ch) {
                buf.append(ch);
            }
            for (char ch = 'a'; ch <= 'z'; ++ch) {
                buf.append(ch);
            }
            JSONLexer lexer = new JSONLexer(buf.toString());
            lexer.nextToken();
            Assert.assertEquals(JSONToken.IDENTIFIER, lexer.token());
        }
    }

    public void test_number() throws Exception {
        String text = "[0,1,-1,2E3,2E+3,2E-3,2e3,2e+3,2e-3]";
        JSONArray array = JSON.parseArray(text);

        Assert.assertEquals(0, array.get(0));
        Assert.assertEquals(1, array.get(1));
        Assert.assertEquals(-1, array.get(2));
        Assert.assertEquals(new BigDecimal("2E3"), array.get(3));
        Assert.assertEquals(new BigDecimal("2E3"), array.get(4));
        Assert.assertEquals(new BigDecimal("2E-3"), array.get(5));
        Assert.assertEquals(new BigDecimal("2E3"), array.get(6));
        Assert.assertEquals(new BigDecimal("2E3"), array.get(7));
        Assert.assertEquals(new BigDecimal("2E-3"), array.get(8));

        for (long i = Long.MIN_VALUE; i <= Long.MIN_VALUE + 1000 * 10; ++i) {
            Assert.assertEquals(i, JSON.parse(Long.toString(i)));
        }

        for (long i = Long.MAX_VALUE - 1000 * 10; i <= Long.MAX_VALUE && i > 0; ++i) {
            Assert.assertEquals(i, JSON.parse(Long.toString(i)));
        }
    }

    public void test_big_integer_1() throws Exception {
        String text = Long.MAX_VALUE + "1234";
        JSONLexer lexer = new JSONLexer(text);
        lexer.nextToken();
        Assert.assertEquals(new BigInteger(text), lexer.integerValue());
    }

    public void test_big_integer_2() throws Exception {
        String text = Long.MIN_VALUE + "1234";
        JSONLexer lexer = new JSONLexer(text);
        lexer.nextToken();
        Assert.assertEquals(new BigInteger(text), lexer.integerValue());
    }

    public void test_big_integer_3() throws Exception {
        String text = "9223372036854775809";
        JSONLexer lexer = new JSONLexer(text);
        lexer.nextToken();
        Assert.assertEquals(new BigInteger(text), lexer.integerValue());
    }

    public void test_error2() {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("--");
            lexer.nextToken();
            lexer.integerValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error3() {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("");
            lexer.nextToken();
            lexer.nextToken();
            lexer.integerValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
