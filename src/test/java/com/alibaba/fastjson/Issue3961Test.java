package com.alibaba.fastjson;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class Issue3961Test {

    @Test
    public void testToJSONStringKeyValuePair() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("key1", "value1");
        mockMap.put("key2", "value2");
        mockMap.put("key3", "value3");
        mockList.add(mockMap);

        String actualOutput = Issue3961.toJSONStringKeyValuePair(mockList);

        String expectedOutput = "[{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testToJSONStringKeyValuePairWithMultipleItems() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("key1", "value1");
        mockMap.put("key2", "value2");
        mockMap.put("key3", "value3");
        mockList.add(mockMap);

        Map<String, Object> mockMap2 = new HashMap<String, Object>();
        mockMap2.put("key4", "value4");
        mockMap2.put("key5", "value5");
        mockMap2.put("key6", "value6");
        mockList.add(mockMap2);

        String actualOutput = Issue3961.toJSONStringKeyValuePair(mockList);

        String expectedOutput = "[{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"},{\"key5\":\"value5\",\"key6\":\"value6\",\"key4\":\"value4\"}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testToJSONStringKeyValuePairWithIntegerValues() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("key1", 1);
        mockMap.put("key2", 2);
        mockMap.put("key3", 3);
        mockList.add(mockMap);

        String actualOutput = Issue3961.toJSONStringKeyValuePair(mockList);

        String expectedOutput = "[{\"key1\":\"1\",\"key2\":\"2\",\"key3\":\"3\"}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testToJSONStringKeyValuePairWithDoubleValues() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("key1", 1.8);
        mockMap.put("key2", 2.6);
        mockMap.put("key3", 3.9);
        mockList.add(mockMap);

        String actualOutput = Issue3961.toJSONStringKeyValuePair(mockList);

        String expectedOutput = "[{\"key1\":\"1.8\",\"key2\":\"2.6\",\"key3\":\"3.9\"}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testToJSONStringKeyValuePairWithObjectAsValues() {
        List<Map<String, Object>> mockList = new ArrayList<>();
        Map<String, Object> mockMap = new HashMap<String, Object>();
        mockMap.put("key1", Double.valueOf(1.8));
        mockMap.put("key2", Double.valueOf(2.6));
        mockMap.put("key3", Double.valueOf(3.9));
        mockMap.put("key4", new String("testValue"));
        mockList.add(mockMap);

        String actualOutput = Issue3961.toJSONStringKeyValuePair(mockList);

        String expectedOutput = "[{\"key1\":\"1.8\",\"key2\":\"2.6\",\"key3\":\"3.9\",\"key4\":\"testValue\"}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testMultiLevelHashMap() {
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();

        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put("key1", 1);
        innerMap.put("key2", 2);
        innerMap.put("key3", 3);

        Map<String, Map<String, Object>> outerMap = new HashMap<>();
        outerMap.put("item1", innerMap);
        outerMap.put("item2", innerMap);

        list.add(outerMap);

        String actualOutput = Issue3961.multiLevelHashMap(list);

        String expectedOutput = "[{\"item2\":{\"key1\":\"1\",\"key2\":\"2\",\"key3\":\"3\"}},{\"item1\":{\"key1\":\"1\",\"key2\":\"2\",\"key3\":\"3\"}}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testMultiLevelHashMapWithOneNullMap() {
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();

        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put("key1", 1);
        innerMap.put("key2", 2);
        innerMap.put("key3", 3);

        Map<String, Map<String, Object>> outerMap = new HashMap<>();
        outerMap.put("item1", null);
        outerMap.put("item2", innerMap);

        list.add(outerMap);

        String actualOutput = Issue3961.multiLevelHashMap(list);

        String expectedOutput = "[{\"item2\":{\"key1\":\"1\",\"key2\":\"2\",\"key3\":\"3\"}},{\"item1\":{}}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testMultiLevelHashMapWithAllNullMaps() {
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();

        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put("key1", 1);
        innerMap.put("key2", 2);
        innerMap.put("key3", 3);

        Map<String, Map<String, Object>> outerMap = new HashMap<>();
        outerMap.put("item1", null);
        outerMap.put("item2", null);

        list.add(outerMap);

        String actualOutput = Issue3961.multiLevelHashMap(list);

        String expectedOutput = "[{\"item2\":{}},{\"item1\":{}}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }

    @Test
    public void testMultiLevelHashMapWithNullOuterMaps() {
        List<Map<String, Map<String, Object>>> list = new ArrayList<>();

        Map<String, Map<String, Object>> outerMap = new HashMap<>();
        outerMap.put(null, null);

        list.add(outerMap);
        String actualOutput = Issue3961.multiLevelHashMap(list);

        String expectedOutput = "[{\"null\":{}}]";
        assertTrue(actualOutput.equals(expectedOutput));
    }
}
