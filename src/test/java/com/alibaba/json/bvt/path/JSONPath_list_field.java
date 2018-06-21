package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_list_field extends TestCase {

    public void test_list_field() throws Exception {
        JSONPath path = new JSONPath("$.name");
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity("wenshao"));
        entities.add(new Entity("ljw2083"));

        List<String> names = (List<String>)path.eval(entities);
        Assert.assertSame(entities.get(0).getName(), names.get(0));
        Assert.assertSame(entities.get(1).getName(), names.get(1));
    }
    
    public void test_list_field_simple() throws Exception {
        JSONPath path = new JSONPath("name");
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity("wenshao"));
        entities.add(new Entity("ljw2083"));
        
        List<String> names = (List<String>) path.eval(entities);
        Assert.assertSame(entities.get(0).getName(), names.get(0));
        Assert.assertSame(entities.get(1).getName(), names.get(1));
    }

    public static class Entity {

        private String name;

        public Entity(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
