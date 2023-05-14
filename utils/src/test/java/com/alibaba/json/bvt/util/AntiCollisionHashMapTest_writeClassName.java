package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.AntiCollisionHashMap;
import junit.framework.TestCase;

import java.util.Map;

public class AntiCollisionHashMapTest_writeClassName extends TestCase {
    public void test_for_bug() throws Exception {
        ParserConfig config = new ParserConfig();
        config.addAccept("com.alibaba.fastjson.util.AntiCollisionHashMap");
        Model m = JSON.parseObject("{\"value\":{\"@type\":\"com.alibaba.fastjson.util.AntiCollisionHashMap\"}}", Model.class, config);
        assertTrue(m.value.getInnerMap() instanceof AntiCollisionHashMap);
    }

    public static class Model {
        public JSONObject value;
    }
}
