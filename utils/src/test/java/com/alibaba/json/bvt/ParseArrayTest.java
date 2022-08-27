package com.alibaba.json.bvt;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ParseArrayTest extends TestCase {
    public void test_0 () throws Exception {
        List<Object> list = JSON.parseArray("[{}, {}]", new Type[] {TreeMap.class, HashMap.class});
        Assert.assertTrue(list.get(0) instanceof TreeMap);
        Assert.assertTrue(list.get(1) instanceof HashMap);
    }
    
    public void test_1 () throws Exception {
        List<Object> list = JSON.parseArray("[1, 2, \"abc\"]", new Type[] {int.class, Integer.class, String.class});
        Assert.assertTrue(list.get(0) instanceof Integer);
        Assert.assertTrue(list.get(1) instanceof Integer);
        Assert.assertTrue(list.get(2) instanceof String);
    }
    
    public void test_2 () throws Exception {
        List<Object> list = JSON.parseArray("[1, null, \"abc\"]", new Type[] {int.class, Integer.class, String.class});
        Assert.assertTrue(list.get(0) instanceof Integer);
        Assert.assertTrue(list.get(1) == null);
        Assert.assertTrue(list.get(2) instanceof String);
    }
}
