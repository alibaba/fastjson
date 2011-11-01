package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;

public class DefaultObjectDeserializerTest1 extends TestCase {

    public void test_0() throws Exception {
        String input = "{'map':{}}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Map<String, Object> map = new HashMap<String, Object>();
        deser.parseMap(parser, map, new TypeReference<TreeMap>() {
        }.getType(), null);
        Assert.assertTrue(map.get("map") instanceof TreeMap);
    }

    public void test_1() throws Exception {
        String input = "{'map':null}";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Map<String, Object> map = new HashMap<String, Object>();
        deser.parseMap(parser, map, new TypeReference<TreeMap>() {
        }.getType(), null);
        Assert.assertTrue(map.get("map") == null);
    }

    public void test_error_0() throws Exception {

        String input = "{\"map\" {} }";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Throwable error = null;

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            deser.parseMap(parser, map, new TypeReference<TreeMap>() {
            }.getType(), null);
        } catch (Throwable ex) {
            error = ex;
        }

        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        String input = "{'map': 'aaa' }";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), 0);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Throwable error = null;

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            deser.parseMap(parser, map, new TypeReference<TreeMap>() {
            }.getType(), null);
        } catch (Throwable ex) {
            error = ex;
        }

        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        String input = "{'map' 'aaa' }";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();

        Throwable error = null;

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            deser.parseMap(parser, map, new TypeReference<TreeMap>() {
            }.getType(), null);
        } catch (Throwable ex) {
            error = ex;
        }

        Assert.assertNotNull(error);
    }
    
    public void test_error_3() throws Exception {
        String input = "{map 'aaa' }";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        DefaultObjectDeserializer deser = new DefaultObjectDeserializer();
        
        Throwable error = null;
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            deser.parseMap(parser, map, new TypeReference<TreeMap>() {
            }.getType(), null);
        } catch (Throwable ex) {
            error = ex;
        }
        
        Assert.assertNotNull(error);
    }
}
