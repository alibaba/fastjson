package com.alibaba.json.bvt.path;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class JSONPath_list_size_1 extends TestCase {
    public void test_obj_array() throws Exception {
        Object[] array = new Object[] {1, 2, 3};
        JSONPath path = new JSONPath("$.size()");
        Integer result = (Integer) path.eval(array);
        Assert.assertEquals(array.length, result.intValue());
    }

    public void test_int_array() throws Exception {
        int[] array = new int[] {1, 2, 3};
        JSONPath path = new JSONPath("$.size()");
        Integer result = (Integer) path.eval(array);
        Assert.assertEquals(array.length, result.intValue());
    }
}
