package com.alibaba.json.bvt.issue_2500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2515 extends TestCase  {
    public void test_for_issue() throws Exception {
        String json = "{\n" +
                "    \"a\":\"{\\\"b\\\":\\\"cd\\\"}\"\n" +
                "}";

        JSONObject obj = JSON.parseObject(json);

        assertEquals("cd", JSONPath.eval(obj, "$.a.b"));
        assertEquals(1, JSONPath.eval(obj, "$.a.size"));
    }
}
