package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;

public class JSONCreatorTest extends TestCase {

    public void test_create() throws Exception {
        Entity entity = new Entity(123, "菜姐");
        String text = JSON.toJSONString(entity);
        
        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(entity.getId(), entity2.getId());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }
    
    public void test_create_2() throws Exception {
        Entity entity = new Entity(123, "菜姐");
        String text = JSON.toJSONString(entity);
        
        ParserConfig config = new ParserConfig();
        config.setAsmEnable(false);
        
        Entity entity2 = JSON.parseObject(text, Entity.class, config, 0);
        Assert.assertEquals(entity.getId(), entity2.getId());
        Assert.assertEquals(entity.getName(), entity2.getName());
    }

    public static class Entity {

        private final int    id;
        private final String name;

        @JSONCreator
        public Entity(@JSONField(name = "id") int id, @JSONField(name = "name") String name){
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
