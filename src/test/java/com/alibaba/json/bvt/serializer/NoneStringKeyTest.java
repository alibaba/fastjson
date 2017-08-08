package com.alibaba.json.bvt.serializer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NoneStringKeyTest extends TestCase {

    public void test_0() throws Exception {
        Map map = new HashMap();
        map.put(1, 101);

        Assert.assertEquals("{1:101}", JSON.toJSONString(map));
    }

    public void test_2() throws Exception {
        Map map = new HashMap();
        map.put(1, 101);

        Assert.assertEquals("{\"1\":101}", JSON.toJSONString(map, SerializerFeature.WriteNonStringKeyAsString));
    }

    public void test_null_0() throws Exception {
        Map map = new HashMap();
        map.put(null, 101);

        Assert.assertEquals("{null:101}", JSON.toJSONString(map));
    }

    public void test_3() throws Exception {
        Map map = new HashMap();
        map.put(null, 101);

        Assert.assertEquals("{\"null\":101}", JSON.toJSONString(map, SerializerFeature.WriteNonStringKeyAsString));
    }
}
