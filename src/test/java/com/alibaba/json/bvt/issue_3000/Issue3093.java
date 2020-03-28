package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue3093 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonStr = "{1:1}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        assertTrue(jsonObject.containsKey(1));
        assertTrue(jsonObject.containsKey("1"));
        assertEquals(1, jsonObject.get(1));
        assertEquals(1, jsonObject.get("1"));
    }
}
