package com.alibaba.json.bvt.issue_2800;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue2831 extends TestCase {
    public void test_for_issue() throws Exception {
        //System.out.println(1);
        Map<Object, Object> map = new HashMap();
        map.put(1, "1");
        map.put(2, 2);
        map.put(Boolean.valueOf("false"), "fa");
        map.put("false", "lse");
        String s = JSONObject.toJSONString(map);
        JSONObject j = JSONObject.parseObject(s);
        j.put("3", null);
        assertEquals(5, j.size());
    }

}
