package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue2130 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = "{\"score\":0.000099369485}";
        JSONObject object = JSON.parseObject(str);
        assertEquals("{\"score\":0.000099369485}", object.toJSONString());
    }
}
