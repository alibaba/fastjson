package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Bug_101_for_rongganlin_case3 extends TestCase {

    public void test_for_bug() throws Exception {
        Entity entity = new Entity();
        entity.setHolder(new Holder<String>("AAA"));

        JSONObject json = (JSONObject) JSON.toJSON(entity);
        Entity entity2 = JSON.toJavaObject(json, Entity.class);
        Assert.assertEquals(JSON.toJSONString(entity), JSON.toJSONString(entity2));
    }

    public static class Entity {

        private Holder<?> holder;

        public Holder<?> getHolder() {
            return holder;
        }

        public void setHolder(Holder<?> holder) {
            this.holder = holder;
        }

    }

    public static class Holder<T> {

        private T value;
        
        public Holder() {
            
        }
        
        public Holder(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

    }
}
