package com.alibaba.json.bvt.path;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

public class JSONPath_array_multi extends TestCase {

    Object[] list = new Object[10];

    public JSONPath_array_multi(){
        for (int i = 0; i < list.length; ++i) {
            list[i] = new Object();
        }
    }

    public void test_list_multi() throws Exception {
        List<Object> result = (List<Object>) new JSONPath("$[2,4,5,8,100]").eval(list);
        Assert.assertEquals(5, result.size());
        Assert.assertSame(list[2], result.get(0));
        Assert.assertSame(list[4], result.get(1));
        Assert.assertSame(list[5], result.get(2));
        Assert.assertSame(list[8], result.get(3));
        Assert.assertNull(result.get(4));
    }

    public void test_list_multi_negative() throws Exception {
        List<Object> result = (List<Object>) new JSONPath("$[-1,-2,-100]")
                .eval(list);
        Assert.assertEquals(3, result.size());
        Assert.assertSame(list[9], result.get(0));
        Assert.assertSame(list[8], result.get(1));
        Assert.assertNull(result.get(2));
    }
}
