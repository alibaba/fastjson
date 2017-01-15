package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class JSONScannerTest_true extends TestCase {
    public void test_scan_true_0() throws Exception {
        JSONLexer lexer = new JSONLexer("true");
        lexer.nextToken();
    }

    public void test_scan_true_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("frue");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_2() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("ttue");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_3() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("trze");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_4() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("truz");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_5() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("truee");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_6() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("true\"");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_7() throws Exception {
        JSONLexer lexer = new JSONLexer("true a");
        lexer.nextToken();
    }

    public void test_scan_true_8() throws Exception {
        JSONLexer lexer = new JSONLexer("true,");
        lexer.nextToken();
    }

    public void test_scan_true_9() throws Exception {
        JSONLexer lexer = new JSONLexer("true\na");
        lexer.nextToken();
    }

    public void test_scan_true_10() throws Exception {
        JSONLexer lexer = new JSONLexer("true\ra");
        lexer.nextToken();
    }

    public void test_scan_true_11() throws Exception {
        JSONLexer lexer = new JSONLexer("true\ta");
        lexer.nextToken();
    }

    public void test_scan_true_12() throws Exception {
        JSONLexer lexer = new JSONLexer("true\fa");
        lexer.nextToken();
    }

    public void test_scan_true_13() throws Exception {
        JSONLexer lexer = new JSONLexer("true\ba");
        lexer.nextToken();
    }

    public void test_scan_false_14() throws Exception {
        JSONLexer lexer = new JSONLexer("true}");
        lexer.nextToken();
    }

    public void test_scan_false_15() throws Exception {
        JSONLexer lexer = new JSONLexer("true]");
        lexer.nextToken();
    }
}
