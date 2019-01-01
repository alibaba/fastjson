package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import junit.framework.TestCase;

public class Issue2182 extends TestCase {
    public void test_for_issue() throws Exception {
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("admin", "admin.create");
        multimap.put("admin", "admin.update");
        multimap.put("admin", "admin.delete");
        multimap.put("user", "user.create");
        multimap.put("user", "user.delete");

        String json = JSON.toJSONString(multimap);
        assertEquals("{\"admin\":[\"admin.create\",\"admin.update\",\"admin.delete\"],\"user\":[\"user.create\",\"user.delete\"]}", json);

        ArrayListMultimap multimap1 = JSON.parseObject(json, ArrayListMultimap.class);

        assertEquals(multimap.size(), multimap1.size());
        assertEquals(json, JSON.toJSONString(multimap1));
    }
}
