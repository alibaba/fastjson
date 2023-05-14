package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONCreatorTest4 extends TestCase {

    public void test_create_error() throws Exception {
        Entity entity = JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":\"$\"}}", Entity.class);
        assertNotNull(entity);
        assertEquals(1001, entity.id);
        assertEquals("wenshao", entity.name);
        assertSame(entity, entity.obj);
    }

    public static class Entity {

        private final int    id;
        private final String name;
        private Entity obj;

        @JSONCreator
        public Entity(@JSONField(name = "id") int id, @JSONField(name = "name") String name,
                      Entity obj){
            this.id = id;
            this.name = name;
            this.obj = obj;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Entity getObj() {
            return obj;
        }

        public void setObj(Entity obj) {
            this.obj = obj;
        }
    }

}
