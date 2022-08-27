package com.alibaba.json.bvt.issue_2300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue2341 extends TestCase {
    public void test_for_issue() throws Exception {
        String ss = "{\"@type\":\"1234\"}";
        JSONObject object = JSON.parseObject(ss);
        assertEquals("1234", object.get("@type"));
    }
}
