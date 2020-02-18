package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Issue1480 extends TestCase {
    public void test_for_issue() throws Exception {

        Map<Integer,Integer> map = new LinkedHashMap<Integer,Integer>();
        map.put(1,10);
        map.put(2,4);
        map.put(3,5);
        map.put(4,5);
        map.put(37306,98);
        map.put(36796,9);

        String json = JSON.toJSONString(map);
        System.out.println(json);
        Assert.assertEquals("{1:10,2:4,3:5,4:5,37306:98,36796:9}",json);

        Map<Integer,Integer> map1 = JSON.parseObject(json,new TypeReference<HashMap<Integer,Integer>>() {});

        Assert.assertEquals(map1.get(Integer.valueOf(1)),Integer.valueOf(10));
        Assert.assertEquals(map1.get(Integer.valueOf(2)),Integer.valueOf(4));
        Assert.assertEquals(map1.get(Integer.valueOf(3)),Integer.valueOf(5));
        Assert.assertEquals(map1.get(Integer.valueOf(4)),Integer.valueOf(5));
        Assert.assertEquals(map1.get(Integer.valueOf(37306)),Integer.valueOf(98));
        Assert.assertEquals(map1.get(Integer.valueOf(36796)),Integer.valueOf(9));

        JSONObject map2 = JSON.parseObject("{35504:1,1:10,2:4,3:5,4:5,37306:98,36796:9\n" + "}");

        Assert.assertEquals(map2.get(Integer.valueOf(1)),Integer.valueOf(10));
        Assert.assertEquals(map2.get(Integer.valueOf(2)),Integer.valueOf(4));
        Assert.assertEquals(map2.get(Integer.valueOf(3)),Integer.valueOf(5));
        Assert.assertEquals(map2.get(Integer.valueOf(4)),Integer.valueOf(5));
        Assert.assertEquals(map2.get(Integer.valueOf(37306)),Integer.valueOf(98));
        Assert.assertEquals(map2.get(Integer.valueOf(36796)),Integer.valueOf(9));

    }
}
