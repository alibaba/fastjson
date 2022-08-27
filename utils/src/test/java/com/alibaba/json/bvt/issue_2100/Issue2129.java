package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.LinkedHashMultimap;
import junit.framework.TestCase;

public class Issue2129 extends TestCase {
    public void test_for_issue() throws Exception {
        LinkedHashMultimap<String, String> map = LinkedHashMultimap.create();
        map.put("a", "1");
        map.put("a", "b");
        map.put("b", "1");
        String json = JSON.toJSONString(map);
        assertEquals("{\"a\":[\"1\",\"b\"],\"b\":[\"1\"]}", json);
    }
}
