package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_list_size extends TestCase {
    public void test_list_size() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        JSONPath path = new JSONPath("$.size()");
        Integer result = (Integer) path.eval(list);
        Assert.assertEquals(list.size(), result.intValue());
    }

    public void test_list_size2() throws Exception {
        List list = new ArrayList();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        JSONPath path = new JSONPath("$.size");
        Integer result = (Integer) path.eval(list);
        Assert.assertEquals(list.size(), result.intValue());
    }
}
