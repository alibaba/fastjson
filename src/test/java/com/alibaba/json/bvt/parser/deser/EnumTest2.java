package com.alibaba.json.bvt.parser.deser;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class EnumTest2 extends TestCase {
    
    public void test_enum() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("{\"value\":\"A\"}", Entity.class).getValue());
    }

    public void test_enum_array() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("[{\"value\":\"A\"}]", Entity[].class)[0].getValue());
    }

    public void test_enum_obj() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("{\"entry\":{\"value\":\"A\"}}", // 
                                                  new TypeReference<Map<String, Entity>>(){}).get("entry").getValue());
    }
    
    public void test_enum_obj_1() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("{\"entry\":{\"value\":\"A\"},\"entryB\":{\"value\":\"B\"}}", // 
                                                  new TypeReference<Map<String, Entity>>(){}).get("entry").getValue());
    }

    public void test_enum_error_slash() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":\"A\\\"}", Entity.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_enum_error_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("12.3", TimeUnit.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static enum E {
        A, B, C
    }

    public static class Entity {

        private E value;

        public Entity(){

        }

        public Entity(E value){
            super();
            this.value = value;
        }

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

    }
}
