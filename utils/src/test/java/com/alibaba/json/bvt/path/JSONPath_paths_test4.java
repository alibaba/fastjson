package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_paths_test4 extends TestCase {
    public void test_map() throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(1001);
        list.add("wenshao");
        
        list.add(Collections.singletonMap("type", "emp"));
        
        Map<String, Object> paths = JSONPath.paths(list);
        
        Assert.assertEquals(5, paths.size());
        Assert.assertSame(list, paths.get("/"));
        Assert.assertEquals(1001, paths.get("/0"));
        Assert.assertEquals("wenshao", paths.get("/1"));
        Assert.assertSame(list.get(2), paths.get("/2"));
        Assert.assertSame(((Map)list.get(2)).get("type"), paths.get("/2/type"));
    }
}
