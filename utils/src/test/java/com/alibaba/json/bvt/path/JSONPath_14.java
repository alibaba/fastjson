package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JSONPath_14
        extends TestCase {

    public void test_0() {
        JSONObject sourceJson = JSON.parseObject("{\n" +
                "\t\"boolean1\":true,\n" +
                "\t\"boolean2\":false,\n" +
                "\t\"boolean3\":true,\n" +
                "\t\"boolean4\":true,\n" +
                "\t\"name\":\"str\",\n" +
                "\t\"name1\":\"str\"\n" +
                "}");

        sourceJson.put("enum1", TimeUnit.SECONDS);
        sourceJson.put("character1", 'A');
        sourceJson.put("uuid1", UUID.randomUUID());

        // 初始配置中，新增的字段添加的库中
        Map<String, Object> paths = JSONPath.paths(sourceJson);
        System.out.println(JSON.toJSONString(paths));
        assertEquals(10, paths.size());

        JSONObject destJson = JSON.parseObject("{\n" +
                "\t\"boolean1\":true,\n" +
                "\t\"boolean2\":false,\n" +
                "\t\"name\":\"str\"\n" +
                "}");

        for (Map.Entry<String, Object> stringObjectEntry : paths.entrySet()) {
            if(stringObjectEntry.getValue() instanceof JSONObject || stringObjectEntry.getValue() instanceof JSONArray){
                continue;
            }
            if (!JSONPath.contains(destJson, stringObjectEntry.getKey())) {
                JSONPath.set(destJson, stringObjectEntry.getKey(), stringObjectEntry.getValue());
                System.out.println("key=" + stringObjectEntry.getKey() + " ,value=" + stringObjectEntry.getValue());
            }
        }

        System.out.println(destJson.toJSONString());
    }


}
