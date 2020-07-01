package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;

public class Issue3082 extends TestCase {
    public void test_for_issue_entry() throws Exception {
        String str = "{\"k\":{\"k\":\"v\"}}";
        Map.Entry<String, Map.Entry<String, String>> entry = JSON.parseObject(str, new TypeReference<Map.Entry<String, Map.Entry<String, String>>>() {});
        assertEquals("v", entry.getValue().getValue());
    }

    public void test_for_issue() throws Exception {
        HashSet<Map.Entry<String, Map.Entry<String, String>>> nestedSet = new HashSet<Map.Entry<String, Map.Entry<String, String>>>();
        nestedSet.add(new AbstractMap.SimpleEntry<String, Map.Entry<String, String>>("a", new AbstractMap.SimpleEntry<String, String>("b", "c")));
        nestedSet.add(new AbstractMap.SimpleEntry<String, Map.Entry<String, String>>("d", new AbstractMap.SimpleEntry<String, String>("e", "f")));

        String content = JSON.toJSONString(nestedSet);

        HashSet<Map.Entry<String, Map.Entry<String, String>>> deserializedNestedSet;
        Type type = new TypeReference<HashSet<Map.Entry<String, Map.Entry<String, String>>>>() {}.getType();
        deserializedNestedSet = JSON.parseObject(content, type);
        assertEquals("b", deserializedNestedSet.iterator().next().getValue().getKey());
    }
}
