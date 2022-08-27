package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;

public class JSONCreatorTest_float_obj extends TestCase {

    public void test_create() throws Exception {
        Entity entity = new Entity(123.45F, "菜姐");
        String text = JSON.toJSONString(entity);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertTrue(entity.getId().floatValue() == entity2.getId().floatValue());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public void test_create_2() throws Exception {
        Entity entity = new Entity(123.45F, "菜姐");
        String text = JSON.toJSONString(entity);

        ParserConfig config = new ParserConfig();

        Entity entity2 = JSON.parseObject(text, Entity.class, config, 0);
        Assert.assertTrue(entity.getId().floatValue() == entity2.getId().floatValue());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public static class Entity {

        private final Float  id;
        private final String name;

        @JSONCreator
        public Entity(@JSONField(name = "id") Float id, @JSONField(name = "name") String name){
            this.id = id;
            this.name = name;
        }

        public Float getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
