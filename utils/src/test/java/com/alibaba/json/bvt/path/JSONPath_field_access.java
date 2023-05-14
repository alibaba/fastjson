package com.alibaba.json.bvt.path;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_field_access extends TestCase {

    public void test_list_map() throws Exception {
        Entity entity = new Entity(123, "wenshao");
        JSONPath path = new JSONPath("$['id']");
        
        Assert.assertSame(entity.getId(), path.eval(entity));
    }

    public static class Entity {

        private Integer id;
        private String  name;

        public Entity(Integer id, String name){
            this.id = id;
            this.name = name;
        }

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
