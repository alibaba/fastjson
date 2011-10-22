package com.alibaba.json.test.bvt.bug;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat7 extends TestCase {

    public void test_date() throws Exception {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);

        Entity entity = new Entity();

        entity.setValue(date);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.test.bvt.bug.Bug_for_smoothrat7$Entity\",\"value\":new Date(" + millis + ")}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(date, entity2.getValue());
        Assert.assertEquals(date.getClass(), entity2.getValue().getClass());
    }
    
    public void test_sqldate() throws Exception {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);

        Entity entity = new Entity();

        entity.setValue(date);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.test.bvt.bug.Bug_for_smoothrat7$Entity\",\"value\":{\"@type\":\"java.sql.Date\",\"val\":" + millis + "}}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(date, entity2.getValue());
        Assert.assertEquals(date.getClass(), entity2.getValue().getClass());
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
