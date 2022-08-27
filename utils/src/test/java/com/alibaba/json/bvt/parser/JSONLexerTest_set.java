package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

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
            JSON.parse("Xet[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
