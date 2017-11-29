package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_list_range extends TestCase {
    public void test_range() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        JSONPath path = new JSONPath("$[2:4]");
        List<Object> result = (List<Object>) path.eval(list);
        Assert.assertEquals(3, result.size());
        Assert.assertSame(list.get(2), result.get(0));
        Assert.assertSame(list.get(3), result.get(1));
        Assert.assertSame(list.get(4), result.get(2));
    }
    
    public void test_range_1() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        JSONPath path = new JSONPath("$[:4]");
        List<Object> result = (List<Object>) path.eval(list);
        Assert.assertEquals(5, result.size());
        Assert.assertSame(list.get(0), result.get(0));
        Assert.assertSame(list.get(1), result.get(1));
        Assert.assertSame(list.get(2), result.get(2));
        Assert.assertSame(list.get(3), result.get(3));
        Assert.assertSame(list.get(4), result.get(4));
    }
    
    public void test_range_2() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());

        JSONPath path = new JSONPath("$[4:]");
        List<Object> result = (List<Object>) path.eval(list);
        Assert.assertEquals(2, result.size());
        Assert.assertSame(list.get(4), result.get(0));
        Assert.assertSame(list.get(5), result.get(1));
    }

    
    public void test_range_step() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        JSONPath path = new JSONPath("$[2:8:2]");
        List<Object> result = (List<Object>) path.eval(list);
        Assert.assertEquals(2, result.size());
        Assert.assertSame(list.get(2), result.get(0));
        Assert.assertSame(list.get(4), result.get(1));
    }
}
