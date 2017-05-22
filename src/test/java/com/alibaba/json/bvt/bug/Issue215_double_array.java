package com.alibaba.json.bvt.bug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class Issue215_double_array extends TestCase {
    public void test_for_issue() throws Exception {
        double[] values = new double[128];
        Random random = new Random();
        for (int i = 0; i < values.length; ++i) {
            values[i] = random.nextDouble();
        }
        
        Map<String, double[]> map = new HashMap<String, double[]>();
        map.put("val", values);
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<String, double[]> map2 = JSON.parseObject(text, new TypeReference<HashMap<String, double[]>>() {});
        double[] values2 = (double[]) map2.get("val");
        Assert.assertTrue(Arrays.equals(values2, values));
    }
}
