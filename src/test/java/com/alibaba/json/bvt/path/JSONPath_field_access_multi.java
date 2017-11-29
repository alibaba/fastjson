package com.alibaba.json.bvt.path;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_field_access_multi extends TestCase {

    public void test_list_map() throws Exception {
        Entity entity = new Entity(123, "wenshao");
        JSONPath path = new JSONPath("$['id','name']");
        
        List<Object> result = (List<Object>) path.eval(entity);
        Assert.assertSame(entity.getId(), result.get(0));
        Assert.assertSame(entity.getName(), result.get(1));
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
