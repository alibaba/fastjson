package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.sql.Time;

public class Issue1644 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", 1324138987429L);

        Time time = jsonObject.getObject("time", java.sql.Time.class);
        assertEquals(1324138987429L, time.getTime());
    }
}
