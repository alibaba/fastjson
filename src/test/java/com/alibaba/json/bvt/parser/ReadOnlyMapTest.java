package com.alibaba.json.bvt.parser;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ReadOnlyMapTest extends TestCase {

    public void test_readOnlyNullList() throws Exception {
        String text = "{\"values\":{\"a\":{}}}";
        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertNotNull(entity);
        Assert.assertNotNull(entity.values.get("a"));
        Assert.assertTrue(entity.values.get("a") instanceof A);
    }

    public static class Entity {

        private final Map<String, A> values = new HashMap<String, A>();

        public Map<String, A> getValues() {
            return values;
        }

    }

    public static class A {
        public A() {
            
        }
    }
}
