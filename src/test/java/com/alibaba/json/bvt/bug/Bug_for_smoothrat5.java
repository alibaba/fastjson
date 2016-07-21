package com.alibaba.json.bvt.bug;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat5 extends TestCase {

    public void test_map() throws Exception {
        Map<Object, Object> map = new LinkedHashMap<Object, Object>();
        map.put(34L, "b");
        map.put(12, "a");

        Entity entity = new Entity();

        entity.setValue(map);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat5$Entity\",\"value\":{\"@type\":\"java.util.LinkedHashMap\",34L:\"b\",12:\"a\"}}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(map, entity2.getValue());
        Assert.assertEquals(map.getClass(), entity2.getValue().getClass());
    }
    
    public void test_treemap() throws Exception {
        TreeMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put(-34L, "b");
        map.put(-56L, "a");
        

        Entity entity = new Entity();

        entity.setValue(map);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat5$Entity\",\"value\":{\"@type\":\"java.util.TreeMap\",-56L:\"a\",-34L:\"b\"}}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(map, entity2.getValue());
        Assert.assertEquals(map.getClass(), entity2.getValue().getClass());
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
