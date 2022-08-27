package com.alibaba.json.bvt.issue_2800;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Issue2830 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = JSONObject.parseObject("{\"qty\":\"10\",\"qty1\":\"10.0\",\"qty2\":\"10.000\"}");

        assertEquals(10, jsonObject.getIntValue("qty"));
        assertEquals(10, jsonObject.getIntValue("qty1"));
        assertEquals(10, jsonObject.getIntValue("qty2"));

    }
}
