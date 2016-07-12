package com.alibaba.json.bvt.path;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_set_test5 extends TestCase {
    public void test_jsonpath_1() throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        
        JSONPath.set(root, "/a[0]/b[0]", 1001);
        
        String json = JSON.toJSONString(root);
        Assert.assertEquals("{\"a\":[{\"b\":[1001]}]}", json);
        Assert.assertEquals(1001, JSONPath.eval(root, "/a[0]/b[0]"));
    }
}
