package com.alibaba.json.bvt.bug;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_melin extends TestCase {
    
    public void test_for_melin() throws Exception {
        Entity object = new Entity();
        object.setId(123);
        object.setName("\\");
        
        String text = JSON.toJSONString(object);
        
        // {"id":123,"name":"\\"}
        Assert.assertEquals("{\"id\":123,\"name\":\"\\\\\"}", text);
    }
    
    public void test_for_melin_() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "\\");
        
        String text = JSON.toJSONString(map);
        
        // {"id":123,"name":"\\"}
        Assert.assertEquals("{\"id\":123,\"name\":\"\\\\\"}", text);
    }

    public static class Entity {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
