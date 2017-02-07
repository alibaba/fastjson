package com.alibaba.json.bvt.guava;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by wenshao on 15/01/2017.
 */
public class ImmutableMapTest extends TestCase {
    public void test_immutableMap() throws Exception {
        Map<String, Integer> map = ImmutableMap.of("a", 1, "b", 2, "c", 3);
        String json = JSON.toJSONString(map);
        assertEquals("{\"a\":1,\"b\":2,\"c\":3}", json);
    }
}
