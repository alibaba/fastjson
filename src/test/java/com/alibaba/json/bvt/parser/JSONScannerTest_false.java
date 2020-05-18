package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONScannerTest_false extends TestCase {

    public void test_scan_false_0() throws Exception {
        JSONScanner lexer = new JSONScanner("false");
        lexer.scanFalse();
    }

    public void test_scan_false_1() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("zalse");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_2() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("fzlse");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_3() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("fazse");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_4() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("falze");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_5() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("falsz");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_6_1() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("falsee");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_6() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("false\"");
            lexer.scanFalse();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_false_7() throws Exception {
        JSONScanner lexer = new JSONScanner("false a");
        lexer.scanFalse();
    }

    public void test_scan_false_8() throws Exception {
        JSONScanner lexer = new JSONScanner("false,");
        lexer.scanFalse();
    }

    public void test_scan_false_9() throws Exception {
        JSONScanner lexer = new JSONScanner("false\na");
        lexer.scanFalse();
    }

    public void test_scan_false_10() throws Exception {
        JSONScanner lexer = new JSONScanner("false\ra");
        lexer.scanFalse();
    }

    public void test_scan_false_11() throws Exception {
        JSONScanner lexer = new JSONScanner("false\ta");
        lexer.scanFalse();
    }

    public void test_scan_false_12() throws Exception {
        JSONScanner lexer = new JSONScanner("false\fa");
        lexer.scanFalse();
    }

    public void test_scan_false_13() throws Exception {
        JSONScanner lexer = new JSONScanner("false\ba");
        lexer.scanFalse();
    }

    public void test_scan_false_14() throws Exception {
        JSONScanner lexer = new JSONScanner("false}");
        lexer.scanFalse();
    }

    public void test_scan_false_15() throws Exception {
        JSONScanner lexer = new JSONScanner("false]");
        lexer.scanFalse();
    }
}
