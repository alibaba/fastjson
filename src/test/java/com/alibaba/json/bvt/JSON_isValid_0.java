package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class JSON_isValid_0 extends TestCase {
    public void test_for_isValid_0() throws Exception {
        assertFalse(JSON.isValid(null));
        assertFalse(JSON.isValid(""));
    }

    public void test_for_isValid_value() throws Exception {
        assertTrue(JSON.isValid("null"));
        assertTrue(JSON.isValid("123"));
        assertTrue(JSON.isValid("12.34"));
        assertTrue(JSON.isValid("true"));
        assertTrue(JSON.isValid("false"));
        assertTrue(JSON.isValid("\"abc\""));
    }


    public void test_for_isValid_obj() throws Exception {
        assertTrue(JSON.isValid("{}"));
        assertTrue(JSON.isValid("{\"id\":123}"));
        assertTrue(JSON.isValid("{\"id\":\"123\"}"));
        assertTrue(JSON.isValid("{\"id\":true}"));
        assertTrue(JSON.isValid("{\"id\":{}}"));
    }

    public void test_for_isValid_obj_1() throws Exception {
        assertTrue(JSON.isValidObject("{}"));
        assertTrue(JSON.isValidObject("{\"id\":123}"));
        assertTrue(JSON.isValidObject("{\"id\":\"123\"}"));
        assertTrue(JSON.isValidObject("{\"id\":true}"));
        assertTrue(JSON.isValidObject("{\"id\":{}}"));
    }

    public void test_for_isValid_array() throws Exception {
        assertTrue(JSON.isValid("[]"));
        assertTrue(JSON.isValid("[[],[]]"));
        assertTrue(JSON.isValid("[{\"id\":123}]"));
        assertTrue(JSON.isValid("[{\"id\":\"123\"}]"));
        assertTrue(JSON.isValid("[{\"id\":true}]"));
        assertTrue(JSON.isValid("[{\"id\":{}}]"));
    }

    public void test_for_isValid_array_1() throws Exception {
        assertTrue(JSON.isValidArray("[]"));
        assertTrue(JSON.isValidArray("[[],[]]"));
        assertTrue(JSON.isValidArray("[{\"id\":123}]"));
        assertTrue(JSON.isValidArray("[{\"id\":\"123\"}]"));
        assertTrue(JSON.isValidArray("[{\"id\":true}]"));
        assertTrue(JSON.isValidArray("[{\"id\":{}}]"));
    }

}
