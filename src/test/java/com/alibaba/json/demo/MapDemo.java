package com.alibaba.json.demo;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MapDemo extends TestCase {
    public void test_0 () throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "张三");
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
    }
}
