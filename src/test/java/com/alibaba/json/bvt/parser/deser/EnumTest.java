package com.alibaba.json.bvt.parser.deser;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public class EnumTest extends TestCase {

    public void test_enum() throws Exception {
        Assert.assertNull(JSON.parseObject("''", TimeUnit.class));
    }

    public void test_enum_1() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("0", E.class));
    }
    
    public void test_enum_3() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("{value:0}", Entity.class).getValue());
    }

    public void test_enum_2() throws Exception {
        Assert.assertEquals(E.A, JSON.parseObject("'A'", E.class));
    }

    public void test_enum_error() throws Exception {
        assertNull(JSON.parseObject("'123'", TimeUnit.class));
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
