package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.UUID;

public class Issue3356 extends TestCase {
    public void test_for_issue() throws Exception {
        UUID uuid = UUID.randomUUID();

        JSONObject object = new JSONObject();
        object.put("1", "1");
        object.put(uuid.toString(), uuid.toString());
        object.put("A", "A");
        object.put("true", "true");
        assertEquals("1", object.get(1));
        assertEquals("true", object.get(true));
        assertEquals("A", object.get('A'));
    }
}
