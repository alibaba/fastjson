package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_set_test2 extends TestCase {

    public void test_jsonpath() throws Exception {
        JSONObject rootObject = JSON.parseObject("{\"array\":[{},{},{},{}]}");
        JSONPath.set(rootObject, "$.array[0:].key", "123");

        JSONArray array = rootObject.getJSONArray("array");
        for (int i = 0; i < array.size(); ++i) {
            Assert.assertEquals("123", array.getJSONObject(i).get("key"));
        }
        System.out.println(rootObject);
    }
}
