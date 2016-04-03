package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class JSONScannerTest_new extends TestCase {
    Method method;
    
    protected void setUp() throws Exception {
        method = JSONLexer.class.getDeclaredMethod("scanNullOrNew");
        method.setAccessible(true);
    }

    public void test_scan_new_0() throws Exception {
        JSONLexer lexer = new JSONLexer("new");
        method.invoke(lexer);
    }

    public void test_scan_new_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nww");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_2() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nee");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_3() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("neel");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_4() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("neww");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_5() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("newe");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_6() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("new\"");
            method.invoke(lexer);
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_7() throws Exception {
        JSONLexer lexer = new JSONLexer("new a");
        method.invoke(lexer);
    }

    public void test_scan_new_8() throws Exception {
        JSONLexer lexer = new JSONLexer("new,");
        method.invoke(lexer);
    }

    public void test_scan_new_9() throws Exception {
        JSONLexer lexer = new JSONLexer("new\na");
        method.invoke(lexer);
    }

    public void test_scan_new_10() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ra");
        method.invoke(lexer);
    }

    public void test_scan_new_11() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ta");
        method.invoke(lexer);
    }

    public void test_scan_new_12() throws Exception {
        JSONLexer lexer = new JSONLexer("new\fa");
        method.invoke(lexer);
    }

    public void test_scan_new_13() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ba");
        method.invoke(lexer);
    }

    public void test_scan_new_14() throws Exception {
        JSONLexer lexer = new JSONLexer("new}");
        method.invoke(lexer);
    }

    public void test_scan_new_15() throws Exception {
        JSONLexer lexer = new JSONLexer("new]");
        method.invoke(lexer);
    }
}
