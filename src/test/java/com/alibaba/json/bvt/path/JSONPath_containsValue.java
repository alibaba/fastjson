package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_containsValue extends TestCase {
    public void test_root() throws Exception {
        List list = new ArrayList();
        list.add("kiki");
        list.add("ljw2083");
        list.add("wenshao");
        
        Assert.assertTrue(JSONPath.containsValue(list, "/0", "kiki"));
        Assert.assertFalse(JSONPath.containsValue(list, "/0", "kiki_"));
        Assert.assertTrue(JSONPath.contains(list, "/"));
        Assert.assertTrue(JSONPath.contains(list, "/0"));
        Assert.assertTrue(JSONPath.contains(list, "/1"));
        Assert.assertTrue(JSONPath.contains(list, "/2"));
        Assert.assertFalse(JSONPath.contains(list, "/3"));
        Assert.assertFalse(JSONPath.contains(null, "$"));
        Assert.assertFalse(JSONPath.containsValue(null, "$", "kiki"));
    }
    
}
