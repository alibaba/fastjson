package com.alibaba.json.bvt;

import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class SpecialKeyTest2 extends TestCase {
    public void test_0() throws Exception {
        Model model = JSON.parseObject("{\"items\":{\"1\":{},\"1001\":{}},\"items1\":{\"$ref\":\"$.items\"}}", Model.class);
        Assert.assertEquals(2, model.items.size());
        Assert.assertNotNull(model.items.get(1L));
        Assert.assertNotNull(model.items.get(1001L));
        Assert.assertSame(model.items, model.items1);
    }
    
    public static class Model {
        public Map<Long, Item> items;
        public Map<Long, Item> items1;
    }
    
    public static class Item {
    }
}
