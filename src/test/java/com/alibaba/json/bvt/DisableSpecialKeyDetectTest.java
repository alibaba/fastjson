package com.alibaba.json.bvt;

import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

public class DisableSpecialKeyDetectTest extends TestCase {

    public void test_0() throws Exception {
        String json = "{\"schema\":{\"$ref\":{\"@title\":\"类目ID\",\"@type\":\"string\"},\"$\":{\"@\":\"类目名称\",\"type\":\"string\"},\"cat_desc\":{\"title\":\"类目描述\",\"type\":\"string\"}}}";
        JSONObject errorJson = JSON.parseObject(json, Feature.DisableSpecialKeyDetect);
        JSONObject schema = errorJson.getJSONObject("schema");
        Set<Map.Entry<String, Object>> es2 = schema.entrySet();
        for (Map.Entry<String, Object> entry : es2) {
            System.out.println(entry.getKey() + "_" + entry.getValue());
        }
    }

    public void test_1() throws Exception {
        String text = "{\"@v1\":\"v1\",\"@type\":\"v2\", \"@\":\"v3\",\"$\":\"v4\",\"$ref\":\"v5\"}";
        JSONObject json = JSON.parseObject(text, Feature.DisableSpecialKeyDetect);
        Assert.assertEquals("v1", json.getString("@v1"));
        Assert.assertEquals("v2", json.getString("@type"));
        Assert.assertEquals("v3", json.getString("@"));
        Assert.assertEquals("v4", json.getString("$"));
        Assert.assertEquals("v5", json.getString("$ref"));
    }
    
    public void test_2() throws Exception {
        String text = "{\"@v1\":\"v1\",\"@type\":\"v2\", \"@\":\"v3\",\"$\":\"v4\",\"$ref\":\"v5\"}";
        Map<String,String> map = JSON.parseObject(text, new TypeReference<Map<String,String>>(){}, Feature.DisableSpecialKeyDetect);
        Assert.assertEquals("v1", map.get("@v1"));
        Assert.assertEquals("v2", map.get("@type"));
        Assert.assertEquals("v3", map.get("@"));
        Assert.assertEquals("v4", map.get("$"));
        Assert.assertEquals("v5", map.get("$ref"));
    }

}
