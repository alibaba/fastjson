package com.alibaba.json.bvt;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class SpecialKeyTest extends TestCase {

    public void test_0() throws Exception {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(1, "a");
        map.put(2, "b");
        
        String text = JSON.toJSONString(map);
        System.out.println(text);
        
        Map<Integer, Object> map2 = JSON.parseObject(text, new TypeReference<Map<Integer, Object>>() {});
        Assert.assertEquals(map, map2);
    }
}
