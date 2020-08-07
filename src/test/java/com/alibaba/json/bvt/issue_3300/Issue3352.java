package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Map;
import java.util.UUID;

public class Issue3352 extends TestCase {
    public void test_for_issue() throws Exception {
        UUID uuid = UUID.randomUUID();

        JSONObject object = new JSONObject();
        Map map = object.getInnerMap();
        map.put("1", "1");
        map.put("A", "A");
        map.put("true", "true");
        map.put(uuid.toString(), uuid);

        assertTrue(object.containsKey(1));
        assertTrue(object.containsKey("1"));
        assertTrue(object.containsKey('A'));
        assertTrue(object.containsKey("A"));
        assertTrue(object.containsKey(true));
        assertTrue(object.containsKey("true"));
        assertTrue(object.containsKey(uuid));
        assertTrue(object.containsKey(uuid.toString()));
    }
}
