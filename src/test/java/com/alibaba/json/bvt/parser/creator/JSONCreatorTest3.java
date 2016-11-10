package com.alibaba.json.bvt.parser.creator;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class JSONCreatorTest3 extends TestCase {

    public void test_create_1() throws Exception {
        Entity vo = JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":\"$\"}}", Entity.class);
        Assert.assertEquals(1001, vo.getId());
        Assert.assertEquals("wenshao", vo.getName());
        Assert.assertSame(vo, vo.getObj());
    }
    
    public void test_create_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":123}}", Entity.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_create_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"id\":1001,\"name\":\"wenshao\",\"obj\":{\"$ref\":\"$\",\"value\":123}}", Entity.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Entity {

        private final int    id;
        private final String name;
        private final Entity obj;

        @JSONCreator
        public Entity(@JSONField(name = "id") int id, @JSONField(name = "name") String name,
                      @JSONField(name = "obj") Entity obj){
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

    }

}
