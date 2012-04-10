package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;
import com.alibaba.fastjson.util.AnticollisionHashMap;

public class DefaultObjectDeserializerTest2 extends TestCase {

    public void test_0() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Map<String, TreeMap> map = JSON.parseObject(input, new TypeReference<Map<String, TreeMap>>() {
        }.getType());

        Assert.assertTrue(map.get("map") instanceof TreeMap);
    }

    public void test_1() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);


        SortedMap<String, SortedMap> map = JSON.parseObject(input, new TypeReference<SortedMap<String, SortedMap>>() {
        }.getType());

        Assert.assertEquals(TreeMap.class, map.get("map").getClass());
    }

    public void test_2() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        SortedMap<String, Map> map = JSON.parseObject(input, new TypeReference<SortedMap<String, Map>>() {
        }.getType());

        Assert.assertEquals(AnticollisionHashMap.class, map.get("map").getClass());
    }

    public void test_3() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        SortedMap<String, ConcurrentMap> map = JSON.parseObject(input, new TypeReference<SortedMap<String, ConcurrentMap>>() {
        }.getType());

        Assert.assertEquals(ConcurrentHashMap.class, map.get("map").getClass());
    }

    public void test_4() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        SortedMap<String, HashMap> map = JSON.parseObject(input, new TypeReference<SortedMap<String, HashMap>>() {
        }.getType());

        Assert.assertEquals(HashMap.class, map.get("map").getClass());
    }

    public void test_5() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        HashMap<String, HashMap> map = JSON.parseObject(input, new TypeReference<HashMap<String, HashMap>>() {
        }.getType());

        Assert.assertEquals(HashMap.class, map.get("map").getClass());
    }

    public void test_6() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Map map = JSON.parseObject(input, new TypeReference<JSONObject>() {
        }.getType());

        Assert.assertEquals(JSONObject.class, map.get("map").getClass());
    }

    public void test_7() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        TreeMap<String, HashMap> map = JSON.parseObject(input, new TypeReference<TreeMap<String, HashMap>>() {
        }.getType());

        Assert.assertEquals(HashMap.class, map.get("map").getClass());
    }

    public void test_8() throws Exception {
        String input = "{'map':{}}";

        ConcurrentMap<String, HashMap> map = JSON.parseObject(input, new TypeReference<ConcurrentMap<String, HashMap>>() {
        }.getType());

        Assert.assertEquals(HashMap.class, map.get("map").getClass());
    }

    public void test_error() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Object val = null;
        Exception error = null;
        try {
            val = JSON.parseObject(input, new TypeReference<Map1<String, HashMap>>() {
            }.getType());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static interface Map1<K, V> extends Map<K, V> {

    }
    
    public static class Map2<K, V> extends HashMap<K, V> {

    }
}
