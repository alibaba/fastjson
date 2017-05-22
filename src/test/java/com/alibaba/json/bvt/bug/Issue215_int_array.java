package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class Issue215_int_array extends TestCase {
    public void test_for_issue() throws Exception {
        int[] values = new int[128];
        Random random = new Random();
        for (int i = 0; i < values.length; ++i) {
            values[i] = random.nextInt();
        }
        
        Map<String, int[]> map = new HashMap<String, int[]>();
        map.put("val", values);
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<String, int[]> map2 = JSON.parseObject(text, new TypeReference<HashMap<String, int[]>>() {});
        int[] values2 = (int[]) map2.get("val");
        Assert.assertArrayEquals(values2, values);
    }
}
