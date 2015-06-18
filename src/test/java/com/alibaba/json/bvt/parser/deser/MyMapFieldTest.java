package com.alibaba.json.bvt.parser.deser;

import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class MyMapFieldTest extends TestCase {

    public void test_null() throws Exception {
        Entity value = JSON.parseObject("{value:null}", Entity.class);
        Assert.assertNull(value.getValue());
    }

    public void test_empty() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{value:{}}", Entity.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Entity {

        private MyMap<Object, Object> value;

        public MyMap<Object, Object> getValue() {
            return value;
        }

        public void setValue(MyMap<Object, Object> value) {
            this.value = value;
        }
    }

    public class MyMap<K, V> extends HashMap<K, V> {

    }
}
