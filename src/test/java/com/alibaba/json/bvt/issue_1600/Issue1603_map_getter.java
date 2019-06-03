package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Issue1603_map_getter extends TestCase {
    public void test_emptyMap() throws Exception {
        Model_1 m = JSON.parseObject("{\"values\":{\"a\":1001}}", Model_1.class);
        assertEquals(0, m.values.size());
    }

    public void test_unmodifiableMap() throws Exception {
        Model_2 m = JSON.parseObject("{\"values\":{\"a\":1001}}", Model_2.class);
        assertEquals(0, m.values.size());
    }

    public static class Model_1 {
        private final Map<String, Object> values = Collections.emptyMap();

        public Map<String, Object> getValues() {
            return values;
        }
    }

    public static class Model_2 {
        private final Map<String, Object> values = Collections.unmodifiableMap(new HashMap<String, Object>());

        public Map<String, Object> getValues() {
            return values;
        }
    }

}
