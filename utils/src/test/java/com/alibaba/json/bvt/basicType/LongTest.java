package com.alibaba.json.bvt.basicType;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class LongTest extends TestCase {
    public void test_array() throws Exception {
        long[] values = new long[] {Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE};
        String text = JSON.toJSONString(values);
        long[] values_2 = JSON.parseObject(text, long[].class);
        Assert.assertEquals(values_2.length, values.length);
        for (int i = 0; i < values.length; ++i) {
            Assert.assertEquals(values[i], values_2[i]);
        }
    }
    
    public void test_map() throws Exception {
        long[] values = new long[] {Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE};
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < values.length; ++i) {
            map.put(Long.toString(i), values[i]);
        }
        
        String text = JSON.toJSONString(map);
        JSONObject obj = JSON.parseObject(text);
        for (int i = 0; i < values.length; ++i) {
            Assert.assertEquals(values[i], ((Number) obj.get(Long.toString(i))).longValue());
        }
    }
}
