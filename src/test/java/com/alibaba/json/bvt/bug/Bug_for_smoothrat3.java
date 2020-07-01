package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat3 extends TestCase {

    public void test_0() throws Exception {
        long millis = System.currentTimeMillis();
        
        java.sql.Time time = new java.sql.Time(millis);
        Entity entity = new Entity();
        
        entity.setValue(new java.sql.Time(millis));
        
        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat3$Entity\",\"value\":{\"@type\":\"java.sql.Time\",\"val\":" + millis + "}}", text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(time, entity2.getValue());
        
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
