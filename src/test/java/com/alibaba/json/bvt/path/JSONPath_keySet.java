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

    public void test_map() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 1);
        map.put("name", 2);
        Collection<?> result = (Collection<?>)JSONPath.eval(map, "$.keySet()");
        Assert.assertEquals(KEY_SET, result);
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

        // Nested case
        result = (Collection<String>)JSONPath.eval(Collections.singletonMap("obj", e), "$.obj.keySet()");
        Assert.assertEquals(KEY_SET, result);

        // age not null
        e.age = 4L;
        result = (Collection<String>)JSONPath.eval(e, "$.keySet()");
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.containsAll(KEY_SET));
        Assert.assertTrue(result.contains("age"));
    }

    public void test_unsupported() {
        Entity e = new Entity();
        e.id = 3L;
        Entity[] array = {e};
        Assert.assertNull(JSONPath.eval(array, "$.keySet()"));
        Assert.assertNull(JSONPath.eval(Collections.singletonList(e), "$.keySet()"));
    }

    /**
     * Demo for wiki
     */
    @SuppressWarnings("unchecked")
    public void demo() {
        Entity e = new Entity();
        e.setId(null);
        e.setName("hello");
        Collection<String> result;

        // id is null, excluded by keySet
        result = (Collection<String>)JSONPath.eval(e, "$.keySet()");
        assertEquals(1, result.size());
        Assert.assertTrue(result.contains("name"));
        Assert.assertEquals(KEY_SET, result);

        e.setId(1L);
        result = (Collection<String>)JSONPath.eval(e, "$.keySet()");
        assertEquals(2, result.size());
        Assert.assertTrue(result.contains("id")); // included
        Assert.assertTrue(result.contains("name"));
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
