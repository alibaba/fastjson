package com.alibaba.fastjson.serializer.issue3940;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gtn1024
 * <p>
 * https://github.com/alibaba/fastjson/issues/3940
 */
public class Issue3940 {
    JSONObject jo;

    @Before
    public void init() {
        jo = new JSONObject();
    }

    @Test
    public void testInteger() {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 2);
        Assert.assertEquals("{\"1\":2}", JSON.toJSONString(map));
    }

    @Test
    public void testFloat() {
        Map<Float, Integer> map = new HashMap<Float, Integer>();
        map.put(1.23F, 2);
        Assert.assertEquals("{\"1.23\":2}", JSON.toJSONString(map));
    }

    @Test
    public void testDouble() {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        map.put(1.23, 2);
        Assert.assertEquals("{\"1.23\":2}", JSON.toJSONString(map));
    }
}
