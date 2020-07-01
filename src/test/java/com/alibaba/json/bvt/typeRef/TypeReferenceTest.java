package com.alibaba.json.bvt.typeRef;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class TypeReferenceTest extends TestCase {

    @SuppressWarnings("rawtypes")
    public void test_0() throws Exception {
        System.out.println(System.getProperties());
        String text;

        {
            Map<String, Bean> map = new HashMap<String, Bean>();
            map.put("a", new Bean(123, "马加爵"));
            map.put("b", new Bean(234, "药家鑫"));
            map.put("c", new Bean(456, "刘大伟"));

            text = JSON.toJSONString(map);
        }

        System.out.println(text);

        {
            Map<String, Bean> map = JSON.parseObject(text, new TypeReference<Map<String, Bean>>() {}); // 注意这里
            Assert.assertEquals(3, map.size());
            Assert.assertEquals(123, ((Bean) map.get("a")).getId());
            Assert.assertEquals(234, ((Bean) map.get("b")).getId());
            Assert.assertEquals(456, ((Bean) map.get("c")).getId());
    
            Assert.assertEquals("马加爵", ((Bean) map.get("a")).getName());
            Assert.assertEquals("药家鑫", ((Bean) map.get("b")).getName());
            Assert.assertEquals("刘大伟", ((Bean) map.get("c")).getName());
        }
        
        {
            Map map = JSON.parseObject(text, new TypeReference<Map>() {}); // 注意这里
            Assert.assertEquals(3, map.size());
            Assert.assertEquals(123, ((JSONObject) map.get("a")).get("id"));
            Assert.assertEquals(234, ((JSONObject) map.get("b")).get("id"));
            Assert.assertEquals(456, ((JSONObject) map.get("c")).get("id"));
            
            Assert.assertEquals("马加爵", ((JSONObject) map.get("a")).get("name"));
            Assert.assertEquals("药家鑫", ((JSONObject) map.get("b")).get("name"));
            Assert.assertEquals("刘大伟", ((JSONObject) map.get("c")).get("name"));
        }
        
        {
            Map map = JSON.parseObject(text, new TypeReference<JSONObject>() {}); // 注意这里
            Assert.assertEquals(3, map.size());
            Assert.assertEquals(123, ((JSONObject) map.get("a")).get("id"));
            Assert.assertEquals(234, ((JSONObject) map.get("b")).get("id"));
            Assert.assertEquals(456, ((JSONObject) map.get("c")).get("id"));
            
            Assert.assertEquals("马加爵", ((JSONObject) map.get("a")).get("name"));
            Assert.assertEquals("药家鑫", ((JSONObject) map.get("b")).get("name"));
            Assert.assertEquals("刘大伟", ((JSONObject) map.get("c")).get("name"));
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
