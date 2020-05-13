package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONScannerTest_true extends TestCase {

    public void test_scan_true_0() throws Exception {
        JSONScanner lexer = new JSONScanner("true");
        lexer.scanTrue();
    }

    public void test_scan_true_1() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("frue");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_2() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("ttue");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_3() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("trze");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_4() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("truz");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_5() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("truee");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_6() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("true\"");
            lexer.scanTrue();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_true_7() throws Exception {
        JSONScanner lexer = new JSONScanner("true a");
        lexer.scanTrue();
    }

    public void test_scan_true_8() throws Exception {
        JSONScanner lexer = new JSONScanner("true,");
        lexer.scanTrue();
    }

    public void test_scan_true_9() throws Exception {
        JSONScanner lexer = new JSONScanner("true\na");
        lexer.scanTrue();
    }

    public void test_scan_true_10() throws Exception {
        JSONScanner lexer = new JSONScanner("true\ra");
        lexer.scanTrue();
    }

    public void test_scan_true_11() throws Exception {
        JSONScanner lexer = new JSONScanner("true\ta");
        lexer.scanTrue();
    }

    public void test_scan_true_12() throws Exception {
        JSONScanner lexer = new JSONScanner("true\fa");
        lexer.scanTrue();
    }

    public void test_scan_true_13() throws Exception {
        JSONScanner lexer = new JSONScanner("true\ba");
        lexer.scanTrue();
    }

    public void test_scan_false_14() throws Exception {
        JSONScanner lexer = new JSONScanner("true}");
        lexer.scanTrue();
    }

    public void test_scan_false_15() throws Exception {
        JSONScanner lexer = new JSONScanner("true]");
        lexer.scanTrue();
    }
}
