package com.alibaba.json.bvt.typeRef;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class TypeReferenceTest2 extends TestCase {

    public void test_0() throws Exception {
        String text;

        {
            List<Bean> list = new ArrayList<Bean>();
            list.add(new Bean(123, "马加爵"));
            list.add(new Bean(234, "药家鑫"));
            list.add(new Bean(456, "刘大伟"));

            text = JSON.toJSONString(list);
        }

        System.out.println(text);

        {
            List<Bean> list = JSON.parseObject(text, new TypeReference<List<Bean>>() {}); // 注意这里
            Assert.assertEquals(3, list.size());
            Assert.assertEquals(123, ((Bean) list.get(0)).getId());
            Assert.assertEquals(234, ((Bean) list.get(1)).getId());
            Assert.assertEquals(456, ((Bean) list.get(2)).getId());

            Assert.assertEquals("马加爵", ((Bean) list.get(0)).getName());
            Assert.assertEquals("药家鑫", ((Bean) list.get(1)).getName());
            Assert.assertEquals("刘大伟", ((Bean) list.get(2)).getName());
        }
        
        {
            JSONArray list = JSON.parseObject(text, new TypeReference<JSONArray>() {}); // 注意这里
            Assert.assertEquals(3, list.size());
            Assert.assertEquals(123, ((JSONObject) list.get(0)).get("id"));
            Assert.assertEquals(234, ((JSONObject) list.get(1)).get("id"));
            Assert.assertEquals(456, ((JSONObject) list.get(2)).get("id"));
            
            Assert.assertEquals("马加爵", ((JSONObject) list.get(0)).get("name"));
            Assert.assertEquals("药家鑫", ((JSONObject) list.get(1)).get("name"));
            Assert.assertEquals("刘大伟", ((JSONObject) list.get(2)).get("name"));
        }
    }

    public static class Bean {

        private int    id;
        private String name;

        public Bean(){

        }

        public Bean(int id, String name){
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
