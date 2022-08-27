package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;

public class JSONCreatorTest_double_obj extends TestCase {

    public void test_create() throws Exception {
        Entity entity = new Entity(123.45D, "菜姐");
        String text = JSON.toJSONString(entity);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertTrue(entity.getId().doubleValue() == entity2.getId().doubleValue());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public void test_create_2() throws Exception {
        Entity entity = new Entity(123.45D, "菜姐");
        String text = JSON.toJSONString(entity);

        ParserConfig config = new ParserConfig();

        Entity entity2 = JSON.parseObject(text, Entity.class, config, 0);
        Assert.assertTrue(entity.getId().doubleValue() == entity2.getId().doubleValue());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public static class Entity {

        private final Double  id;
        private final String name;

        @JSONCreator
        public Entity(@JSONField(name = "id") Double id, @JSONField(name = "name") String name){
            this.id = id;
            this.name = name;
        }

        public Double getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
