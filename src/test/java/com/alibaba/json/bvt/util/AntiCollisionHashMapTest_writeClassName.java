package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.AntiCollisionHashMap;
import junit.framework.TestCase;

import java.util.Map;

public class AntiCollisionHashMapTest_writeClassName extends TestCase {
    public void test_for_bug() throws Exception {
        Model m = JSON.parseObject("{\"value\":{\"@type\":\"com.alibaba.fastjson.util.AntiCollisionHashMap\"}}", Model.class);
        assertTrue(m.value.getInnerMap() instanceof AntiCollisionHashMap);
    }

    public static class Model {
        public JSONObject value;
    }
}
