package com.alibaba.json.bvt.path;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_list_size_2 extends TestCase {
    public void test_map() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("1001", 1001);
        map.put("1002", 1002);
        JSONPath path = new JSONPath("$.size()");
        Integer result = (Integer) path.eval(map);
        Assert.assertEquals(map.size(), result.intValue());
    }

    public void test_map_null() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("1001", 1001);
        map.put("1002", 1002);
        map.put("1003", null);
        JSONPath path = new JSONPath("$.size()");
        Integer result = (Integer) path.eval(map);
        Assert.assertEquals(2, result.intValue());
    }
}
