package com.alibaba.json.test.bvt;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeConfig;

public class JSONTest_overflow extends TestCase {

    public void test_overflow() throws Exception {
        Entity entity = new Entity();
        entity.setSelf(entity);

        JSONException error = null;
        try {
            JSON.toJSONString(entity, SerializeConfig.getGlobalInstance());
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_overflow_1() throws Exception {
        Entity entity = new Entity();
        entity.setSelf(entity);

        JSONException error = null;
        try {
            JSON.toJSONStringZ(entity, SerializeConfig.getGlobalInstance());
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class Entity {

        private Entity self;

        public Entity getSelf() {
            return self;
        }

        public void setSelf(Entity self) {
            this.self = self;
        }

    }
}
