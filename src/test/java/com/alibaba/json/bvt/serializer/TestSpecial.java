package com.alibaba.json.bvt.serializer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestSpecial extends TestCase {
    @SuppressWarnings("deprecation")
    public void test_0 () throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "\n\r\t");
        System.out.println(JSON.toJSONString(map, SerializerFeature.WriteTabAsSpecial));
    }
}
