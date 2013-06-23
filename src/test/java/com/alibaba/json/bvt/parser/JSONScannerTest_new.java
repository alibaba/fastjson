package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONScannerTest_new extends TestCase {

    public void test_scan_new_0() throws Exception {
        JSONScanner lexer = new JSONScanner("new");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_1() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nww");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_2() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nee");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_3() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("neel");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_4() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("neww");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_5() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("newe");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_6() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("new\"");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_new_7() throws Exception {
        JSONScanner lexer = new JSONScanner("new a");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_8() throws Exception {
        JSONScanner lexer = new JSONScanner("new,");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_9() throws Exception {
        JSONScanner lexer = new JSONScanner("new\na");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_10() throws Exception {
        JSONScanner lexer = new JSONScanner("new\ra");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_11() throws Exception {
        JSONScanner lexer = new JSONScanner("new\ta");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_12() throws Exception {
        JSONScanner lexer = new JSONScanner("new\fa");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_13() throws Exception {
        JSONScanner lexer = new JSONScanner("new\ba");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_14() throws Exception {
        JSONScanner lexer = new JSONScanner("new}");
        lexer.scanNullOrNew();
    }

    public void test_scan_new_15() throws Exception {
        JSONScanner lexer = new JSONScanner("new]");
        lexer.scanNullOrNew();
    }
}
