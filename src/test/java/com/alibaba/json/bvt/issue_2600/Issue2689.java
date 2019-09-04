package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class Issue2689 extends TestCase
{
    public void test_0() throws Exception {
        Exception error = null;
        try {
            JSON.parse("{\"val\":\"\\x~");
        } catch (JSONException ex) {
            error = ex;
        }
        assertTrue(
                error.getMessage().startsWith("invalid escape character"));
    }
}
