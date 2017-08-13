package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 03/08/2017.
 */
public class Issue1362 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject object = new JSONObject();
        object.put("val", "null");
        assertEquals(0D, object.getDoubleValue("val"));
        assertEquals(0F, object.getFloatValue("val"));
        assertEquals(0, object.getIntValue("val"));
        assertEquals(0L, object.getLongValue("val"));
        assertEquals((short) 0, object.getShortValue("val"));
        assertEquals((byte) 0, object.getByteValue("val"));
        assertEquals(false, object.getBooleanValue("val"));
    }
}
