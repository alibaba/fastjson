package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONLexer;

/**
 * parseLong
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_long extends TestCase {

    public void ftest_parse_long() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer("1293770846476");
        lexer.scanNumber();
        Assert.assertEquals(new Long(1293770846476L), (Long) lexer.integerValue());
        Assert.assertEquals(1293770846476L, lexer.longValue());
    }

    public void ftest_parse_long_1() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer(Long.toString(Long.MAX_VALUE));
        lexer.scanNumber();
        Assert.assertEquals(new Long(Long.MAX_VALUE), (Long) lexer.integerValue());
        Assert.assertEquals(Long.MAX_VALUE, lexer.longValue());
    }

    public void test_parse_long_2() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer(Long.toString(Long.MIN_VALUE));
        lexer.scanNumber();
        Assert.assertEquals(new Long(Long.MIN_VALUE), (Long) lexer.integerValue());
        Assert.assertEquals(Long.MIN_VALUE, lexer.longValue());
    }

    public void test_error_0() {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("--");
            lexer.scanNumber();
            lexer.longValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() {
        Exception error = null;
        try {
            String text = Long.MAX_VALUE + "1234";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.longValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() {
        Exception error = null;
        try {
            String text = Long.MIN_VALUE + "1234";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.longValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() {
        Exception error = null;
        try {
            String text = "9223372036854775809";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.longValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
