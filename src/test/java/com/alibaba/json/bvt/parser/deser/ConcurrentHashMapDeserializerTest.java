package com.alibaba.json.bvt.parser.deser;

import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ConcurrentHashMapDeserializerTest extends TestCase {
    @SuppressWarnings("rawtypes")
    public void test_1 () throws Exception {
        ConcurrentHashMap map = JSON.parseObject("{}", ConcurrentHashMap.class);
        Assert.assertEquals(0, map.size());
    }

    @SuppressWarnings("rawtypes")
    public void test_2() throws Exception {
        ConcurrentMap map = JSON.parseObject("{}", ConcurrentMap.class);
        Assert.assertEquals(0, map.size());
    }
    
    @SuppressWarnings("rawtypes")
    public void test_className() throws Exception {
        ConcurrentHashMap map = (ConcurrentHashMap) JSON.parse("{\"@type\":\"java.util.concurrent.ConcurrentHashMap\"}");
        Assert.assertEquals(0, map.size());
    }
    
    @SuppressWarnings("rawtypes")
    public void test_className1() throws Exception {
        IdentityHashMap map = (IdentityHashMap) JSON.parse("{\"@type\":\"java.util.IdentityHashMap\"}");
        Assert.assertEquals(0, map.size());
    }
    
    @SuppressWarnings("rawtypes")
    public void test_className2() throws Exception {
        IdentityHashMap map = (IdentityHashMap) JSON.parse("{\"@type\":\"java.util.IdentityHashMap\", \"id\":123}");
        Assert.assertEquals(1, map.size());
    }
    
    public void test_null () throws Exception {
        Assert.assertEquals(null, JSON.parseObject("null", ConcurrentHashMap.class));
        Assert.assertEquals(null, JSON.parseObject("null", ConcurrentMap.class));
    }
}
