package com.alibaba.json.bvt.parser;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ReadOnlyCollectionTest_final_field extends TestCase {
    
    public void test_readOnlyNullList() throws Exception {
        String text = "{\"list\":[1,2,3]}";
        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertNull(entity.list);
    }

    public static class Entity {

        public final List<Object> list = null;

    }
}
