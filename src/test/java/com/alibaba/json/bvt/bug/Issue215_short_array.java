package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class Issue215_short_array extends TestCase {
    public void test_for_issue() throws Exception {
        short[] values = new short[128];
        Random random = new Random();
        for (int i = 0; i < values.length; ++i) {
            values[i] = (short) random.nextInt();
        }
        
        Map<String, short[]> map = new HashMap<String, short[]>();
        map.put("val", values);
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<String, short[]> map2 = JSON.parseObject(text, new TypeReference<HashMap<String, short[]>>() {});
        short[] values2 = (short[]) map2.get("val");
        Assert.assertArrayEquals(values2, values);
    }
}
