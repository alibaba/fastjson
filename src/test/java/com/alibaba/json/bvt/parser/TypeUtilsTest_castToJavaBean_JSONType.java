package com.alibaba.json.bvt.parser;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_castToJavaBean_JSONType extends TestCase {

    public void test_castToJavaBean() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "abc");
        VO vo = TypeUtils.castToJavaBean(map, VO.class, null);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals("abc", vo.getName());
        
        Assert.assertEquals("{\"name\":\"abc\",\"id\":123}", JSON.toJSONString(vo));
    }
    
    public void test_castToJavaBean_v2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "abc");
        V2 vo = TypeUtils.castToJavaBean(map, V2.class, null);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals("abc", vo.getName());
        
        Assert.assertEquals("{\"name\":\"abc\",\"id\":123}", JSON.toJSONString(vo));
    }
    
    public void test_castToJavaBean_v3() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "abc");
        V3 vo = TypeUtils.castToJavaBean(map, V3.class, null);
        Assert.assertEquals(123, vo.getId());
        Assert.assertEquals("abc", vo.getName());
        
        Assert.assertEquals("{\"name\":\"abc\",\"id\":123}", JSON.toJSONString(vo));
    }

    @JSONType(orders={"name", "id"})
    public static class VO {

        private int    id;
        private String name;

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
    
    @JSONType(orders={"name"})
    public static class V2 {

        private int    id;
        private String name;

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
    
    @JSONType(orders={"name","xx"})
    public static class V3 {
        
        private int    id;
        private String name;
        
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
