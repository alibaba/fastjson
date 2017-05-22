package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_none_root extends TestCase {

    public void test_root() throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(new Object());
        Assert.assertSame(list.get(0), new JSONPath("[0]").eval(list));
    }

    public void test_null() throws Exception {
        Assert.assertNull(new JSONPath("name").eval(null));
    }

    public void test_map() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("val", new Object());
        Assert.assertSame(map.get("val"), new JSONPath("val").eval(map));
    }
    
    public void test_entity() throws Exception {
        Entity entity = new Entity();
        entity.setValue(new Object());
        Assert.assertSame(entity.getValue(), new JSONPath("value").eval(entity));
    }

    public static class Entity {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }
}
