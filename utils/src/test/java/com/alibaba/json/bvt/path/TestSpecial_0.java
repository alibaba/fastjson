package com.alibaba.json.bvt.path;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;


public class TestSpecial_0 extends TestCase {
    public void test_special() throws Exception {
        Map<String, Object> vo = new HashMap<String, Object>();
        
        vo.put("a.b", 123);
        
        Assert.assertEquals((Integer) vo.get("a.b"), (Integer) JSONPath.eval(vo, "a\\.b"));
    }

}
