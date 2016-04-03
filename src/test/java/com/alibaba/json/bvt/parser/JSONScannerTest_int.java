package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.JSONLexer;

/**
 * parseInt
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONScannerTest_int extends TestCase {

    public void ftest_parse_long() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer("1293770846");
        lexer.scanNumber();
        Assert.assertEquals(new Integer(1293770846), (Integer) lexer.integerValue());
        Assert.assertEquals(1293770846, lexer.intValue());
    }

    public void ftest_parse_long_1() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer(Integer.toString(Integer.MAX_VALUE));
        lexer.scanNumber();
        Assert.assertEquals(new Integer(Integer.MAX_VALUE), (Integer) lexer.integerValue());
        Assert.assertEquals(Integer.MAX_VALUE, lexer.intValue());
    }

    public void test_parse_long_2() throws Exception {
        System.out.println(System.currentTimeMillis());
        JSONLexer lexer = new JSONLexer(Long.toString(Integer.MIN_VALUE));
        lexer.scanNumber();
        Assert.assertEquals(new Integer(Integer.MIN_VALUE), (Integer) lexer.integerValue());
        Assert.assertEquals(Integer.MIN_VALUE, lexer.intValue());
    }

    public void test_error_0() {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("--");
            lexer.scanNumber();
            lexer.intValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() {
        Exception error = null;
        try {
            String text = Integer.MAX_VALUE + "1234";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.intValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() {
        Exception error = null;
        try {
            String text = Integer.MIN_VALUE + "1234";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.intValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() {
        Exception error = null;
        try {
            String text = "2147483648";
            JSONLexer lexer = new JSONLexer(text);
            lexer.scanNumber();
            lexer.intValue();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
