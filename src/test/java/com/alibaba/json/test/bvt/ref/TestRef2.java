package com.alibaba.json.test.bvt.ref;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

public class TestRef2 extends TestCase {

    public void test_0() throws Exception {
        String text = JSON.toJSONString(new Entity(123, new Child()));
        System.out.println(text);
        JSON.parseObject(text, Entity.class);
    }

    public static class Entity {

        private final int   id;
        private final Child child;

        @JSONCreator
        public Entity(@JSONField(name = "id") int id, @JSONField(name = "child") Child b){
            super();
            this.id = id;
            this.child = b;
            b.setParent(this);
        }

        public int getId() {
            return id;
        }

        public Child getChild() {
            return child;
        }

    }

    public static class Child {

        private Entity parent;

        public Entity getParent() {
            return parent;
        }

        public void setParent(Entity parent) {
            this.parent = parent;
        }

    }
}
