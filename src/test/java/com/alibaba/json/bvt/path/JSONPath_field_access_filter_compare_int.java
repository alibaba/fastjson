package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_field_access_filter_compare_int extends TestCase {

    public void test_list_map() throws Exception {
        JSONPath path = new JSONPath("$[?(@.id <= 1002)]");

        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));

        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(2, result.size());
        Assert.assertSame(entities.get(0), result.get(0));
        Assert.assertSame(entities.get(1), result.get(1));
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
