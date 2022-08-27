package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_field_access_filter_compare_string_simple extends TestCase {

    public void test_list_eq() throws Exception {
        JSONPath path = new JSONPath("[name = 'ljw2083']");

        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));

        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(1, result.size());
        Assert.assertSame(entities.get(0), result.get(0));
    }
    
    public void test_list_eq_x() throws Exception {
        JSONPath path = new JSONPath("[name = 'ljw2083']");
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));
        
        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(1, result.size());
        Assert.assertSame(entities.get(0), result.get(0));
    }
    
    public void test_list_eq_null() throws Exception {
        JSONPath path = new JSONPath("$[name = null]");

        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));

        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(2, result.size());
        Assert.assertSame(entities.get(2), result.get(0));
        Assert.assertSame(entities.get(3), result.get(1));
    }
    
    public void test_list_not_null() throws Exception {
        JSONPath path = new JSONPath("$[name != null]");
        
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
    
    public void test_list_gt() throws Exception {
        JSONPath path = new JSONPath("$[name > 'ljw2083']");
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));
        
        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(1, result.size());
        Assert.assertSame(entities.get(1), result.get(0));
    }
    
    public void test_list_ge() throws Exception {
        JSONPath path = new JSONPath("$[name >= 'ljw2083']");
        
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

    public void test_list_lt() throws Exception {
        JSONPath path = new JSONPath("$[?(@.name < 'wenshao')]");
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(1001, "ljw2083"));
        entities.add(new Entity(1002, "wenshao"));
        entities.add(new Entity(1003, null));
        entities.add(new Entity(null, null));
        
        List<Object> result = (List<Object>) path.eval(entities);
        Assert.assertEquals(1, result.size());
        Assert.assertSame(entities.get(0), result.get(0));
    }
    
    public void test_list_le() throws Exception {
        JSONPath path = new JSONPath("$[name <= 'wenshao']");
        
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
