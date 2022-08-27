package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_between_int extends TestCase {
    public void test_between() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        List<Object> result = (List<Object>) JSONPath.eval(list, "$[id between 101 and 101]");
        Assert.assertEquals(1, result.size());
        Assert.assertSame(list.get(0), result.get(0));
    }
    
    public void test_between_2() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        List<Object> result = (List<Object>) JSONPath.eval(list, "$[id between 101 and 102]");
        Assert.assertEquals(2, result.size());
        Assert.assertSame(list.get(0), result.get(0));
        Assert.assertSame(list.get(1), result.get(1));
    }
    
    public void test_between_not() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        List<Object> result = (List<Object>) JSONPath.eval(list, "$[id not between 101 and 102]");
        Assert.assertEquals(1, result.size());
        Assert.assertSame(list.get(2), result.get(0));
    }

    public static class Entity {

        private Integer id;
        private String  name;

        public Entity(Integer id, String name){
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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
