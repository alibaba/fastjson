package com.alibaba.json.bvt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class CastTest2 extends TestCase {

    public void test_0() throws Exception {
        String text;
        {
            List<Object> list = new ArrayList<Object>();

            list.add(new Header());

            Body body = new Body("张三");
            body.getItems().put("1", new Item());

            list.add(body);

            text = JSON.toJSONString(list);
            
            System.out.println(text);
        }

        JSONArray array = JSON.parseArray(text);

        Body body = array.getObject(1, Body.class);

        Assert.assertEquals("张三", body.getName());
        Assert.assertEquals(1, body.getItems().size());
    }

    public static class Header {

    }

    public static class Body {

        private String name;

        public Body(){

        }

        public Body(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private Map<String, Item> items = new HashMap<String, Item>();

        public Map<String, Item> getItems() {
            return items;
        }

        public void setItems(Map<String, Item> items) {
            this.items = items;
        }
    }

    public static class Item {

    }
}
