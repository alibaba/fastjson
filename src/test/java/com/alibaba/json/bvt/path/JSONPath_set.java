package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

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
