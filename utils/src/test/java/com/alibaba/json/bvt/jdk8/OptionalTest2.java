package com.alibaba.json.bvt.jdk8;

import java.util.Optional;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OptionalTest2 extends TestCase {

    public void test_optional() throws Exception {
        Entity entity = new Entity();
        entity.setValue(Optional.of(123));

        String text = JSON.toJSONString(entity);

        Assert.assertEquals("{\"value\":123}", text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        
        Assert.assertEquals(entity.getValue().get(), entity2.getValue().get());
    }

    public static class Entity {

        private Optional<Integer> value;

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

    }
}
