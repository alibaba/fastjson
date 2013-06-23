package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExtendsTest extends TestCase {

    public void test_extends() throws Exception {
        B b = new B();
        b.setId(123);
        b.setName("加爵");

        JSONObject json = JSON.parseObject(JSON.toJSONString(b));
        Assert.assertEquals(b.getId(), json.get("id"));
        Assert.assertEquals(b.getName(), json.get("name"));
    }

    public static class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    public static class B extends A {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
