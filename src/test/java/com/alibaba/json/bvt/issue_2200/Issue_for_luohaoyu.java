package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue_for_luohaoyu extends TestCase {
    public void test_for_issue() throws Exception {
        Map map = new HashMap();
        map.put(null, 123);

        String str = JSON.toJSONString(map);
        assertEquals("{null:123}", str);

        JSONObject object = JSON.parseObject(str);
    }
}
