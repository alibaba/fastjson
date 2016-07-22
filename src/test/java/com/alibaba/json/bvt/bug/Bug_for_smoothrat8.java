package com.alibaba.json.bvt.bug;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat8 extends TestCase {

    public void test_set() throws Exception {
        Map<Integer, Object> map = new LinkedHashMap<Integer, Object>();
        map.put(1, "a");
        map.put(2, "b");

        Entity entity = new Entity();

        entity.setValue(map);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat8$Entity\",\"value\":{\"@type\":\"java.util.LinkedHashMap\",1:\"a\",2:\"b\"}}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(map, entity2.getValue());
        Assert.assertEquals(map.getClass(), entity2.getValue().getClass());
        Assert.assertEquals(Integer.class, ((Map)entity2.getValue()).keySet().iterator().next().getClass());
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
