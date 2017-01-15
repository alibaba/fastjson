package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class JSONScannerTest_new extends TestCase {

    public void test_scan_new_0() throws Exception {
        JSONLexer lexer = new JSONLexer("new");
        lexer.nextToken();
    }

    public void test_scan_new_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nww");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_2() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("nee");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_3() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("neel");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_4() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("neww");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_5() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("newe");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_6() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("new\"");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_7() throws Exception {
        JSONLexer lexer = new JSONLexer("new a");
        lexer.nextToken();
    }

    public void test_scan_new_8() throws Exception {
        JSONLexer lexer = new JSONLexer("new,");
        lexer.nextToken();
    }

    public void test_scan_new_9() throws Exception {
        JSONLexer lexer = new JSONLexer("new\na");
        lexer.nextToken();
    }

    public void test_scan_new_10() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ra");
        lexer.nextToken();
    }

    public void test_scan_new_11() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ta");
        lexer.nextToken();
    }

    public void test_scan_new_12() throws Exception {
        JSONLexer lexer = new JSONLexer("new\fa");
        lexer.nextToken();
    }

    public void test_scan_new_13() throws Exception {
        JSONLexer lexer = new JSONLexer("new\ba");
        lexer.nextToken();
    }

    public void test_scan_new_14() throws Exception {
        JSONLexer lexer = new JSONLexer("new}");
        lexer.nextToken();
    }

    public void test_scan_new_15() throws Exception {
        JSONLexer lexer = new JSONLexer("new]");
        lexer.nextToken();
    }
}
