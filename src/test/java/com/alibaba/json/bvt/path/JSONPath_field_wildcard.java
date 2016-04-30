package com.alibaba.json.bvt.path;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_field_wildcard extends TestCase {

    public void test_list_map() throws Exception {
        JSONPath path = new JSONPath("$.*");
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "wenshao");

        Collection<Object> fieldValues = (Collection<Object>) path.eval(map);
        Iterator<Object> it = fieldValues.iterator();
        Assert.assertSame(map.get("id"), it.next());
        Assert.assertSame(map.get("name"), it.next());
    }
    
    public void test_list_map_none_root() throws Exception {
        JSONPath path = new JSONPath("*");
        Entity entity = new Entity(123, "wenshao");
        
        List<Object> fieldValues = (List<Object>) path.eval(entity);
        Assert.assertSame(entity.getId(), fieldValues.get(0));
        Assert.assertSame(entity.getName(), fieldValues.get(1));
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
