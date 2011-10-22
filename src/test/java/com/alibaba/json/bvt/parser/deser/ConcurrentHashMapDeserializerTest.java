package com.alibaba.json.bvt.parser.deser;

import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ConcurrentHashMapDeserializer;

public class ConcurrentHashMapDeserializerTest extends TestCase {
    @SuppressWarnings("rawtypes")
    public void test_0 () throws Exception {
        ConcurrentHashMap map = JSON.parseObject("{}", ConcurrentHashMap.class);
        Assert.assertEquals(0, map.size());
        Assert.assertEquals(JSONToken.LBRACE, ConcurrentHashMapDeserializer.instance.getFastMatchToken());
    }
    
    @SuppressWarnings("rawtypes")
    public void test_1 () throws Exception {
        ConcurrentHashMap map = JSON.parseObject("null", ConcurrentHashMap.class);
        Assert.assertEquals(null, map);
    }
}
