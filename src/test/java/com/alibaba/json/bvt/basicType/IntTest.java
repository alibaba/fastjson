package com.alibaba.json.bvt.basicType;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class IntTest extends TestCase {
    public void test_array() throws Exception {
        int[] values = new int[] {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        String text = JSON.toJSONString(values);
        long[] values_2 = JSON.parseObject(text, long[].class);
        assertEquals(values_2.length, values.length);
        for (int i = 0; i < values.length; ++i) {
            assertEquals(values[i], values_2[i]);
        }
    }
    
    public void test_map() throws Exception {
        int[] values = new int[] {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < values.length; ++i) {
            map.put(Integer.toString(i), values[i]);
        }
        
        String text = JSON.toJSONString(map);
        JSONObject obj = JSON.parseObject(text);
        for (int i = 0; i < values.length; ++i) {
            assertEquals(values[i], ((Number) obj.get(Integer.toString(i))).intValue());
        }
    }
}
