package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_list_multi extends TestCase {
    public void test_list_map() throws Exception {
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
        List<Object> result = (List<Object>) new JSONPath("$[2,4,5,8,100]").eval(list);
        Assert.assertEquals(5, result.size());
        Assert.assertSame(list.get(2), result.get(0));
        Assert.assertSame(list.get(4), result.get(1));
        Assert.assertSame(list.get(5), result.get(2));
        Assert.assertSame(list.get(8), result.get(3));
        Assert.assertNull(result.get(4));
    }

    
}
