package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_remove_test extends TestCase {

    public void test_remove() throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("name", "wenshao");
        root.put("salary", 1234567890);
        Assert.assertTrue(JSONPath.remove(root, "/name"));
        Assert.assertEquals(1, root.size());
        Assert.assertFalse(root.containsKey("name"));
        Assert.assertTrue(root.containsKey("salary"));
        Assert.assertFalse(JSONPath.remove(root, "/name"));
    }

    public void test_remove_list() throws Exception {
        List<Object> root = new ArrayList<Object>();
        root.add("wenshao");
        root.add(1234567890);
        
        Assert.assertTrue(JSONPath.remove(root, "/0"));
        Assert.assertEquals(1, root.size());
        
        Assert.assertEquals(1234567890, root.get(0));
        
        Assert.assertFalse(JSONPath.remove(root, "/1"));
    }
}
