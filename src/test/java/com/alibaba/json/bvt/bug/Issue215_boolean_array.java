package com.alibaba.json.bvt.bug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class Issue215_boolean_array extends TestCase {
    public void test_for_issue() throws Exception {
        boolean[] values = new boolean[128];
        Random random = new Random();
        for (int i = 0; i < values.length; ++i) {
            values[i] = random.nextInt() % 2 == 0;
        }
        
        Map<String, boolean[]> map = new HashMap<String, boolean[]>();
        map.put("val", values);
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<String, boolean[]> map2 = JSON.parseObject(text, new TypeReference<HashMap<String, boolean[]>>() {});
        boolean[] values2 = (boolean[]) map2.get("val");
        Assert.assertTrue(Arrays.equals(values2, values));
    }
}
