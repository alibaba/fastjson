package com.alibaba.json.test.bvt.parser.deser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.codehaus.jackson.type.TypeReference;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;

public class FieldDeserializerTest3 extends TestCase {

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            String input = "{\"value\":null}";
            int featureValues = 0;
            DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(),
                                                                   featureValues);

            Entity object = new Entity();
            parser.parseObject(object);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            String input = "{,,\"value\":null}";
            int featureValues = 0;
            DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(),
                                                                   featureValues);

            Entity object = new Entity();
            parser.parseObject(object);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            Type type = new TypeReference<List<?>>() {
            }.getType();

            String input = "{,,\"value\":null}";
            int featureValues = 0;
            DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(),
                                                                   featureValues);

            DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
            objectDeser.deserialze(parser, ((ParameterizedType) type).getActualTypeArguments()[0]);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            Type type = new TypeReference<List<Integer[]>>() {
            }.getType();

            String input = "{,,\"value\":null}";
            int featureValues = 0;
            DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(),
                                                                   featureValues);

            DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
            objectDeser.deserialze(parser, ((ParameterizedType) type).getActualTypeArguments()[0]);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        Exception error = null;
        try {
            Type type = new TypeReference<List<List<Integer[]>>>() {
            }.getType();

            String input = "{,,\"value\":null}";
            int featureValues = 0;
            DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(),
                                                                   featureValues);

            DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
            objectDeser.deserialze(parser, ((ParameterizedType) type).getActualTypeArguments()[0]);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_null() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), featureValues);

        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        Object value = objectDeser.deserialze(parser, Class.class);
        Assert.assertNull(value);
    }

    public void test_hashmap() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{}", ParserConfig.getGlobalInstance(), featureValues);

        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        HashMap value = objectDeser.deserialze(parser, HashMap.class);
        Assert.assertEquals(0, value.size());
    }

    public void test_treeMap() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{}", ParserConfig.getGlobalInstance(), featureValues);

        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        TreeMap value = objectDeser.deserialze(parser, TreeMap.class);
        Assert.assertEquals(0, value.size());
    }

    public void test_concurrentMap() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{}", ParserConfig.getGlobalInstance(), featureValues);

        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        ConcurrentHashMap value = objectDeser.deserialze(parser, ConcurrentHashMap.class);
        Assert.assertEquals(0, value.size());
    }

    public void test_concurrentMap_error() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{[[[", ParserConfig.getGlobalInstance(), featureValues);

        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        Exception error = null;
        try {
            ConcurrentHashMap value = objectDeser.deserialze(parser, ConcurrentHashMap.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Entity {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            throw new RuntimeException();
        }

    }

}
