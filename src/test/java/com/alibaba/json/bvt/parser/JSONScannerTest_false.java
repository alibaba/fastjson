package com.alibaba.json.bvt.parser;

import java.lang.reflect.Method;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONLexer;

import junit.framework.TestCase;

public class JSONScannerTest_false extends TestCase {
   

    public void test_scan_false_0() throws Exception {
        JSONLexer lexer = new JSONLexer("false");
        lexer.nextToken();
    }

    public void test_scan_false_2() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("fzlse");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_3() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("fazse");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_4() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("falze");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_5() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("falsz");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_6_1() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("falsee");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_6() throws Exception {
        Exception error = null;
        try {
            JSONLexer lexer = new JSONLexer("false\"");
            lexer.nextToken();
        } catch (Exception e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_7() throws Exception {
        JSONLexer lexer = new JSONLexer("false a");
        lexer.nextToken();
    }

    public void test_scan_false_8() throws Exception {
        JSONLexer lexer = new JSONLexer("false,");
        lexer.nextToken();
    }

    public void test_scan_false_9() throws Exception {
        JSONLexer lexer = new JSONLexer("false\na");
        lexer.nextToken();
    }

    public void test_scan_false_10() throws Exception {
        JSONLexer lexer = new JSONLexer("false\ra");
        lexer.nextToken();
    }

    public void test_scan_false_11() throws Exception {
        JSONLexer lexer = new JSONLexer("false\ta");
        lexer.nextToken();
    }

    public void test_scan_false_12() throws Exception {
        JSONLexer lexer = new JSONLexer("false\fa");
        lexer.nextToken();
    }

    public void test_scan_false_13() throws Exception {
        JSONLexer lexer = new JSONLexer("false\ba");
        lexer.nextToken();
    }

    public void test_scan_false_14() throws Exception {
        JSONLexer lexer = new JSONLexer("false}");
        lexer.nextToken();
    }

    public void test_scan_false_15() throws Exception {
        JSONLexer lexer = new JSONLexer("false]");
        lexer.nextToken();
    }
}
