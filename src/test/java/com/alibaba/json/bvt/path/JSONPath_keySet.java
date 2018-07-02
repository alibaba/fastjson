package com.alibaba.json.bvt.path;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;
import org.junit.Assert;

public class JSONPath_keySet extends TestCase {

    public static final Set<String> KEY_SET = new HashSet<String>();

    static {
        KEY_SET.add("id");
        KEY_SET.add("name");
    }

    public void test_map() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 1);
        map.put("name", 2);
        Collection<?> result = (Collection<?>)JSONPath.eval(map, "$.keySet()");
        Assert.assertEquals(KEY_SET, result);
    }

    public void test_object() {
        Entity e = new Entity();
        e.id = 3L;
        e.setName("hello");
        System.out.println(JSON.toJSONString(e));
        Collection<?> result = (Collection<?>)JSONPath.eval(e, "$.keySet()");
        Assert.assertEquals(KEY_SET, result);
    }

    public void test_jsonStr() {
        String str = "{\"id\":3,\"name\":\"hello\"}";
        Collection<?> result = (Collection<?>)JSONPath.eval(str, "$.keySet()");
        Assert.assertEquals(KEY_SET, result);
    }

    public void test_unsupported() {
        Entity e = new Entity();
        e.id = 3L;
        Entity[] array = {e};
        Assert.assertNull(JSONPath.eval(array, "$.keySet()"));
        Assert.assertNull(JSONPath.eval(Collections.singletonList(e), "$.keySet()"));
    }

    public static class Entity {
        public Long id;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
