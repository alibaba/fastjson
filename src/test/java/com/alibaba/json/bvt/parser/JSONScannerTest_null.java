package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONScannerTest_null extends TestCase {

    public void test_scan_null_0() throws Exception {
        JSONScanner lexer = new JSONScanner("null");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_1() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("zull");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_2() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nzll");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_3() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nuzl");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_4() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nulz");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_5() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("nulle");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_6() throws Exception {
        JSONException error = null;
        try {
            JSONScanner lexer = new JSONScanner("null\"");
            lexer.scanNullOrNew();
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_scan_null_7() throws Exception {
        JSONScanner lexer = new JSONScanner("null a");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_8() throws Exception {
        JSONScanner lexer = new JSONScanner("null,");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_9() throws Exception {
        JSONScanner lexer = new JSONScanner("null\na");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_10() throws Exception {
        JSONScanner lexer = new JSONScanner("null\ra");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_11() throws Exception {
        JSONScanner lexer = new JSONScanner("null\ta");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_12() throws Exception {
        JSONScanner lexer = new JSONScanner("null\fa");
        lexer.scanNullOrNew();
    }

    public void test_scan_null_13() throws Exception {
        JSONScanner lexer = new JSONScanner("null\ba");
        lexer.scanNullOrNew();
    }

    public void test_scan_false_14() throws Exception {
        JSONScanner lexer = new JSONScanner("null}");
        lexer.scanNullOrNew();
    }

    public void test_scan_false_15() throws Exception {
        JSONScanner lexer = new JSONScanner("null]");
        lexer.scanNullOrNew();
    }
}
