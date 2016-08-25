package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/25.
 */
public class Issue780 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject json = new JSONObject();
        json.put("robj", "{abc: 123}");
        JSONObject robj = json.getJSONObject("robj");
        assertEquals(123, robj.get("abc"));
    }
}
