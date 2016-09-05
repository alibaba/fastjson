package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONCreatorTest2 extends TestCase {

    public void test_create() throws Exception {
        Entity vo = JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":\"..\"}}", Entity.class);
        Assert.assertEquals(1001, vo.getId());
        Assert.assertEquals("wenshao", vo.getName());
        Assert.assertSame(vo, vo.getObj());
    }

    public void test_create_1() throws Exception {
        Entity vo = JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":\"$\"}}", Entity.class);
        Assert.assertEquals(1001, vo.getId());
        Assert.assertEquals("wenshao", vo.getName());
        Assert.assertSame(vo, vo.getObj());
    }

    public static class Entity {

        private final int    id;
        private final String name;
        private final Object obj;

        @JSONCreator
        public Entity(@JSONField(name = "id") int id, @JSONField(name = "name") String name,
                      @JSONField(name = "obj") Object obj){
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

        public Object getObj() {
            return obj;
        }

    }

}
