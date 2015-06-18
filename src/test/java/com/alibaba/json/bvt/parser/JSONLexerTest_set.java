package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONLexerTest_set extends TestCase {

    public void test_treeSet() throws Exception {
        JSON.parse("Set[]");
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parse("S_t[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
   
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parse("Se_[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parse("Set_[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            new JSONScanner("Xet[]").scanSet();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
