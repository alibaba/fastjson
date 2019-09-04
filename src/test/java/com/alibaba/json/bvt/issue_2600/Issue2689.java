package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class Issue2689 extends TestCase
{
    public void test_0() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"val\":\"\\x~\"");
        } catch (JSONException ex) {
            error = ex;
        }
        assertTrue(
                error.getMessage().startsWith("invalid escape character"));
    }

    public void test_1() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"val\":'\\x~'");
        } catch (JSONException ex) {
            error = ex;
        }
        assertTrue(
                error.getMessage().startsWith("invalid escape character"));
    }

    public void test_2() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"val\":'\\x1'");
        } catch (JSONException ex) {
            error = ex;
        }
        assertTrue(
                error.getMessage().startsWith("invalid escape character"));
    }

    public void test_3() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"val\":'\\x'");
        } catch (JSONException ex) {
            error = ex;
        }
        assertTrue(
                error.getMessage().startsWith("invalid escape character"));
    }
}
