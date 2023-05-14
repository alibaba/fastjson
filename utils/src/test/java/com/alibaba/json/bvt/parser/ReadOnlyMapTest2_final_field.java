package com.alibaba.json.bvt.parser;

import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ReadOnlyMapTest2_final_field extends TestCase {

    public void test_readOnlyNullList() throws Exception {
        String text = "{\"values\":{\"a\":{}}}";
        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertNotNull(entity);
        Assert.assertNull(entity.values);
    }

    public static class Entity {

        public final Map<String, A> values = null;


    }

    public static class A {

    }
}
