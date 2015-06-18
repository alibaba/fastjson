package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONScanner;

public class JSONLexerTest_7 extends TestCase {

    public void test_treeSet() throws Exception {
        JSON.parse("TreeSet[]");
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parse("T_eeSet[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            JSON.parse("Tr_eSet[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parse("Tre_Set[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            JSON.parse("Tree_et[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            JSON.parse("TreeS_t[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_5() throws Exception {
        Exception error = null;
        try {
            JSON.parse("TreeSe_[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_6() throws Exception {
        Exception error = null;
        try {
            JSON.parse("TreeSet_[]");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_7() throws Exception {
        Exception error = null;
        try {
            new JSONScanner("XreeSet[]").scanTreeSet();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
