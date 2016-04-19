package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class Bug_for_issue_364 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = new JSONObject(3, true);
        jsonObject.put("name", "J.K.SAGE");
        jsonObject.put("age", 21);
        jsonObject.put("msg", "Hello!");
        JSONObject cloneObject = (JSONObject) jsonObject.clone();
        assertEquals(JSON.toJSONString(jsonObject), JSON.toJSONString(cloneObject));
    }
}
