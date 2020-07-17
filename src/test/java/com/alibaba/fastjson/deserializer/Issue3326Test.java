package com.alibaba.fastjson.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * @Author: lolo
 * @Date: 2020/7/17 0017
 */
public class Issue3326Test {

    @Test
    public void test() {
        LinkedHashMap<Integer, Number> map = new LinkedHashMap<>();
        map.put(1, 1);
        map.put(2, 2.2);
        String json = JSON.toJSONString(map);

        Object obj = JSON.parse(json);
        Assert.assertEquals(((JSONObject) obj).get(2).getClass(),Double.class);

        Object obj2 = JSON.parse(json, Feature.UseBigDecimal);
        Assert.assertEquals(((JSONObject) obj2).get(2).getClass(), BigDecimal.class);
    }

}
