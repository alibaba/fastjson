package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/27.
 */
public class Issue793 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{ \"code\": 1000, \"data\": \"success\", \"game_data\": [], \"member_list\": [], \"message\": \"\\u6210\\u529f\" }";
        JSONObject json = JSON.parseObject(text);
        assertEquals(1000, json.get("code"));
    }
}
