package com.alibaba.json.bvt.bug;

import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class Bug_for_issue_491 extends TestCase {

    public void test_for_issue() throws Exception {
       String json = "{id:1,keyword:[{uuid:\"ddd\"},{uuid:\"zzz\"}]}";
       Map<String, String> map = getJsonToMap1(json, String.class);
       Assert.assertEquals("1", map.get("id"));
    }

    
    public void test_for_issue_2() throws Exception {
        String json = "{1:{name:\"ddd\"},2:{name:\"zzz\"}}";
        Map<Integer, Model> map = getJsonToMap(json, Integer.class, Model.class);
        Assert.assertEquals("ddd", map.get(1).name);
        Assert.assertEquals("zzz", map.get(2).name);
     }
   
    public static class Model {
        public String name;
    }
    
    public static <V> Map<String, V> getJsonToMap1(String json, Class<V> valueType) {
        return JSON.parseObject(json, new TypeReference<Map<String, V>>(valueType) {});
    }
    
    public static <K, V> Map<K, V> getJsonToMap(String json, Class<K> keyType, Class<V> valueType) {
        return JSON.parseObject(json, new TypeReference<Map<K, V>>(keyType, valueType) {});
    }
}
