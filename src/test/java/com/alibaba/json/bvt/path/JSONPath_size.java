package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.json.bvt.path.JSONPath_between_int.Entity;

import junit.framework.TestCase;

public class JSONPath_size extends TestCase {
    public void test_root() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        
        Assert.assertEquals(3, JSONPath.size(list, "$"));
    }
    
    public void test_path() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
         
        JSONObject root = new JSONObject();
        root.put("values", list);
        
        Assert.assertEquals(3, JSONPath.size(root, "$.values"));
    }
    
    public void test_path_size() throws Exception {
        JSONPath path = JSONPath.compile("$");
        
        Assert.assertEquals(-1, path.size(null));
    }
    
    public void test_path_size_1() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        
        JSONPath path = JSONPath.compile("$");
        
        Assert.assertEquals(3, path.size(list));
    }
    
    
    public void test_path_size_2() throws Exception {
        List list = new ArrayList();
        list.add(new Entity(101, "kiki"));
        list.add(new Entity(102, "ljw2083"));
        list.add(new Entity(103, "ljw2083"));
        
        JSONObject root = new JSONObject();
        root.put("values", list);
        
        JSONPath path = JSONPath.compile("$.values");
        
        Assert.assertEquals(3, path.size(root));
    }
}
