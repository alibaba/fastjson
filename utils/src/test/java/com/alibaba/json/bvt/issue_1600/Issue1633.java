package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue1633 extends TestCase {
    public void test_for_issue_int() throws Exception {
        String text = "{123:\"abc\"}";
        JSONObject obj = JSON.parseObject(text, Feature.NonStringKeyAsString);
        assertEquals("abc", obj.getString("123"));
    }

    public void test_for_issue_bool() throws Exception {
        String text = "{false:\"abc\"}";
        JSONObject obj = JSON.parseObject(text, Feature.NonStringKeyAsString);
        assertEquals("abc", obj.getString("false"));
    }
}