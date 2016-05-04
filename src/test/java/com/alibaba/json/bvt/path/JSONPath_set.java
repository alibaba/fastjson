package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;

import junit.framework.TestCase;

public class JSONPath_set extends TestCase {

    public void test_set() throws Exception {
        Entity entity = new Entity();

        JSONPath.set(entity, "$.name", "abc");

        Assert.assertEquals("abc", entity.getName());
    }

    public void test_set_array() throws Exception {
        Object[] array = new Object[1];

        JSONPath.set(array, "[0]", "abc");

        Assert.assertEquals("abc", array[0]);
    }

    public void test_set_list() throws Exception {
        List array = new ArrayList();
        array.add(null);
        array.add(null);

        JSONPath.set(array, "[0]", "abc");

        Assert.assertEquals("abc", array.get(0));
    }

    public void test_root_null() throws Exception {
        Assert.assertFalse(JSONPath.set(null, "[0]", "abc"));
    }
    
    public void test_object_not_exits() throws Exception {
        Assert.assertFalse(JSONPath.set(Collections.singletonMap("values", null), "$.values[0]", "abc"));
    }

    public void test_error() throws Exception {
        Map<String, Object> root = Collections.singletonMap("values", new Object());

        Exception error = null;
        try {
            JSONPath.set(root, "$.values[0]", "abc");
        } catch (JSONPathException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    static class Entity {

        private Integer id;
        private String  name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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
