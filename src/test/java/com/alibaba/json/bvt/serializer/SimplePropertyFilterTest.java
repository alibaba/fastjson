package com.alibaba.json.bvt.serializer;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyFilter;

public class SimplePropertyFilterTest extends TestCase {

    private VO vo;
    
    private A a;
    
    private Map<String, Object> map;

    protected void setUp() throws Exception {
        vo = new VO();
        vo.setId(123);
        vo.setName("sandzhangtoo");
        
        a = new A();
        a.setId(123);
        a.setName("sandzhangtoo");
        
        map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "sandzhangtoo");
        map.put(null, null);
    }

    public void test_name() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class, "name");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_name_0() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter("name");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_name_a() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class, "name");
        Assert.assertEquals("{\"id\":123,\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(a, filter));
    }
    
    public void test_name_a1() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter("name");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(a, filter));
    }
    
    public void test_id() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class, "id");
        Assert.assertEquals("{\"id\":123}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_id_0() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter("id");
        Assert.assertEquals("{\"id\":123}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_map() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class, "name");
        Assert.assertEquals("{\"id\":123,\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(map, filter));
    }
    
    public void test_map_id() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter("id");
        Assert.assertEquals("{\"id\":123}", JSON.toJSONStringWithPropertyFilter(map, filter));
    }
    
    public void test_map_name() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter("name");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(map, filter));
    }
    
    public void test_all() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class);
        Assert.assertEquals("{\"id\":123,\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_all_map() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class);
        Assert.assertEquals("{\"id\":123,\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(map, filter));
    }
    
    public void test_exclude_id() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class);
        filter.getExcludes().add("id");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_exclude_id_map() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class);
        filter.getExcludes().add("id");
        Assert.assertEquals("{\"name\":\"sandzhangtoo\"}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }
    
    public void test_exclude_name() throws Exception {
        SimplePropertyFilter filter = new SimplePropertyFilter(VO.class);
        filter.getExcludes().add("name");
        Assert.assertEquals("{\"id\":123}", JSON.toJSONStringWithPropertyFilter(vo, filter));
    }

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
    
    public static class A {
        
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
