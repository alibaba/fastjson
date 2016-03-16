package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_smoothrat2 extends TestCase {

    public void test_0() throws Exception {
        long millis = System.currentTimeMillis();
        
        java.util.Date time = new java.util.Date(millis);
        Entity entity = new Entity();
        
        entity.setValue(new java.util.Date(millis));
        
        String text = JSON.toJSONString(entity);
        Assert.assertEquals("{\"value\":" + millis + "}", text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(time, entity2.getValue());
        
    }

    public static class Entity {

        private java.util.Date value;

        public java.util.Date getValue() {
            return value;
        }

        public void setValue(java.util.Date value) {
            this.value = value;
        }

    }
}
