package com.alibaba.json.bvt.typeRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class TypeReferenceTest3 extends TestCase {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void test_0() throws Exception {
        String text;

        {
            List list = new ArrayList();
            List mapList = new ArrayList();
            
            Map<String, Bean> map = new HashMap<String, Bean>();
            map.put("a", new Bean(123, "马加爵"));
            map.put("b", new Bean(234, "药家鑫"));
            map.put("c", new Bean(456, "刘＋伟"));
            
            mapList.add(map);
            
            list.add(mapList);

            text = JSON.toJSONString(list);
        }

        System.out.println(text);

        // text = [[{"b":{"name":"药家鑫","id":234},"c":{"name":"刘＋伟","id":456},"a":{"name":"马加爵","id":123}}]]
        List<List<Map<String, Bean>>> list = JSON.parseObject(text, new TypeReference< List<List<Map<String, Bean>>> >() {}); // 注意这里
        Map<String, Bean> map = list.get(0).get(0);
        
        Assert.assertEquals(3, map.size());
        
        Assert.assertEquals(123, ((Bean) map.get("a")).getId());
        Assert.assertEquals(234, ((Bean) map.get("b")).getId());
        Assert.assertEquals(456, ((Bean) map.get("c")).getId());

        Assert.assertEquals("马加爵", ((Bean) map.get("a")).getName());
        Assert.assertEquals("药家鑫", ((Bean) map.get("b")).getName());
        Assert.assertEquals("刘＋伟", ((Bean) map.get("c")).getName());
        
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
        public int getId() {return id;}
        public void setId(int id) { this.id = id;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
    }
}
