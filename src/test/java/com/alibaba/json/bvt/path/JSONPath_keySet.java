package com.alibaba.json.bvt.path;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;
import org.junit.Assert;

public class JSONPath_keySet extends TestCase {

    public static final Set<String> KEY_SET = new HashSet<String>();

    static {
        KEY_SET.add("id");
        KEY_SET.add("name");
    }

    @SuppressWarnings("unchecked")
    public void test_map() {
        Map<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("id", 1);
        map1.put("name", null); // null will be included
        Assert.assertEquals(KEY_SET, JSONPath.eval(map1, "$.keySet()"));
        Assert.assertEquals(KEY_SET, JSONPath.keySet(map1, "$"));

        Map<Long, String> map2 = new HashMap<Long, String>();
        map2.put(1L, "a");
        map2.put(2L, "b");
        Set<Long> keys2 = (Set<Long>)JSONPath.eval(map2, "$.keySet()");
        Assert.assertEquals(2, keys2.size());
        Assert.assertTrue(keys2.contains(1L));
        Assert.assertTrue(keys2.contains(2L));
    }

    @SuppressWarnings("unchecked")
    public void test_object() {
        Entity e = new Entity();
        e.id = 3L;
        e.name = "hello";
        Collection<String> result = null;
        // age is null
        result = (Collection<String>)JSONPath.eval(e, "$.keySet()");
        Assert.assertEquals(KEY_SET, result);

        // age not null
        e.age = 4L;
        result = (Collection<String>)JSONPath.eval(e, "$.keySet()");
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.containsAll(KEY_SET));
        Assert.assertTrue(result.contains("age"));
    }

    public void test_nested() {
        Entity e = new Entity();
        e.id = 3L;
        e.name = "hello";
        Object obj = Collections.singletonMap("obj", e);
        Assert.assertEquals(KEY_SET, JSONPath.eval(obj, "$.obj.keySet()"));
        Assert.assertEquals(KEY_SET, new JSONPath("$.obj").keySet(obj));
    }

    public void test_unsupported() {
        Entity e = new Entity();
        e.id = 3L;
        Entity[] array = {e};
        Map<String, Entity[]> map = Collections.singletonMap("array", array);
        Assert.assertEquals(array, JSONPath.eval(map, "$.array"));
        Assert.assertNull(JSONPath.eval(map, "$.array.keySet()"));
        Assert.assertNull(JSONPath.keySet(map, "$.array"));
        Assert.assertNull(new JSONPath("$.array").keySet(map));
    }

    public void test_null() {
        Assert.assertNull(JSONPath.eval(null, "$.keySet()"));
        Set<?> keySet = (Set<?>)JSONPath.eval(new HashMap<String, Object>(), "$.keySet()");
        Assert.assertEquals(0, keySet.size());
    }

    /**
     * Demo for wiki
     */
    @SuppressWarnings("unchecked")
    public void test_demo() {
        Entity e = new Entity();
        e.setId(null);
        e.setName("hello");
        Map<String, Entity> map = Collections.singletonMap("e", e);
        Collection<String> result;

        // id is null, excluded by keySet
        result = (Collection<String>)JSONPath.eval(map, "$.e.keySet()");
        assertEquals(1, result.size());
        Assert.assertTrue(result.contains("name"));

        e.setId(1L);
        result = (Collection<String>)JSONPath.eval(map, "$.e.keySet()");
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains("id")); // included
        Assert.assertTrue(result.contains("name"));

        // Same result
        Assert.assertEquals(result, JSONPath.keySet(map, "$.e"));
        Assert.assertEquals(result, new JSONPath("$.e").keySet(map));
    }

    public static class Entity {
        private Long id;
        private String name;
        public Long age;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
