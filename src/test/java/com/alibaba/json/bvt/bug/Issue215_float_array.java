package com.alibaba.json.bvt.bug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class Issue215_float_array extends TestCase {
    public void test_for_issue() throws Exception {
        float[] values = new float[128];
        Random random = new Random();
        for (int i = 0; i < values.length; ++i) {
            values[i] = random.nextFloat();
        }
        
        Map<String, float[]> map = new HashMap<String, float[]>();
        map.put("val", values);
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<String, float[]> map2 = JSON.parseObject(text, new TypeReference<HashMap<String, float[]>>() {});
        float[] values2 = (float[]) map2.get("val");
        Assert.assertTrue(Arrays.equals(values2, values));
    }
}
