package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class JSONScannerTest_null extends TestCase {

    Method method;

    protected void setUp() throws Exception {
        method = JSONLexer.class.getDeclaredMethod("scanNullOrNew");
        method.setAccessible(true);
    }

    public void test_scan_null_0() throws Exception {
        JSONLexer lexer = new JSONLexer("null");
        method.invoke(lexer);
    }

    public void test_scan_null_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("zull");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_2() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nzll");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_3() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nuzl");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_4() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nulz");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_5() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nulle");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_6() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("null\"");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_7() throws Exception {
        JSONLexer lexer = new JSONLexer("null a");
        method.invoke(lexer);
    }

    public void test_scan_null_8() throws Exception {
        JSONLexer lexer = new JSONLexer("null,");
        method.invoke(lexer);
    }

    public void test_scan_null_9() throws Exception {
        JSONLexer lexer = new JSONLexer("null\na");
        method.invoke(lexer);
    }

    public void test_scan_null_10() throws Exception {
        JSONLexer lexer = new JSONLexer("null\ra");
        method.invoke(lexer);
    }

    public void test_scan_null_11() throws Exception {
        JSONLexer lexer = new JSONLexer("null\ta");
        method.invoke(lexer);
    }

    public void test_scan_null_12() throws Exception {
        JSONLexer lexer = new JSONLexer("null\fa");
        method.invoke(lexer);
    }

    public void test_scan_null_13() throws Exception {
        JSONLexer lexer = new JSONLexer("null\ba");
        method.invoke(lexer);
    }

    public void test_scan_false_14() throws Exception {
        JSONLexer lexer = new JSONLexer("null}");
        method.invoke(lexer);
    }

    public void test_scan_false_15() throws Exception {
        JSONLexer lexer = new JSONLexer("null]");
        method.invoke(lexer);
    }
}
