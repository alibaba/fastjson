package com.alibaba.json.bvt.path;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_paths_test3 extends TestCase {
    public void test_map() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "wenshao";
        model.attributes.put("type", "employee");
        
        Map<String, Object> paths = JSONPath.paths(model);
        
        Assert.assertEquals(5, paths.size());
        Assert.assertSame(model, paths.get("/"));
        Assert.assertEquals(1001, paths.get("/id"));
        Assert.assertEquals("wenshao", paths.get("/name"));
        Assert.assertSame(model.attributes, paths.get("/attributes"));
        Assert.assertEquals("employee", paths.get("/attributes/type"));
    }
    
    public static class Model {
        public int id;
        public String name;
        
        public Map<String, Object> attributes = new HashMap<String, Object>();
    }
}
