package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ：Nanqi
 * @Date ：Created in 22:29 2020/7/15
 */
public class Issue3347 extends TestCase {
    public void test_for_issue() throws Exception {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "hello");
        String mapJSONString = JSON.toJSONString(map);
        Map mapValues = JSONObject.parseObject(mapJSONString, Map.class);
        Object mapKey = mapValues.keySet().iterator().next();
        assertTrue(mapKey instanceof Integer);
    }
}
