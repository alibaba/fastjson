package com.alibaba.json.test.bvt.parser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class JSONCreatorFactoryTest extends TestCase {

    public void test_create() throws Exception {
        Entity entity = new Entity(123, "菜姐");
        String text = JSON.toJSONString(entity);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(entity.getId(), entity2.getId());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public static class Entity {

        private final int    id;
        private final String name;

        @JSONCreator
        public static Entity create(@JSONField(name = "id") int id, @JSONField(name = "name") String name) {
            return new Entity(id, name);
        }

        private Entity(int id, String name){
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
