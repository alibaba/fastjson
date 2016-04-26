package com.alibaba.json.bvt.parser.deser.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ArrayLisMapDeserializerTest extends TestCase {

    public void test_list() throws Exception {
        Entity a = JSON.parseObject("{items:[{}, {a:1}, null]}", Entity.class);
        Assert.assertEquals(0, a.getItems().get(0).size());
        Assert.assertEquals(1, a.getItems().get(1).size());
        Assert.assertEquals(null, a.getItems().get(2));
    }
    
    public void test_list_2() throws Exception {
        List<Map> list = JSON.parseObject("[{}, {a:1}, null]", new TypeReference<List<Map>>() {});
        Assert.assertEquals(0, list.get(0).size());
        Assert.assertEquals(1, list.get(1).size());
        Assert.assertEquals(null, list.get(2));
    }

    public static class Entity {

        private List<Map> items = new ArrayList<Map>();

        public List<Map> getItems() {
            return items;
        }

        public void setItems(List<Map> items) {
            this.items = items;
        }

    }
}
