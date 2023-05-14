package com.alibaba.json.bvt.ref;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class RefTest5 extends TestCase {
    
    public void test_ref() throws Exception {
        Object[] array = new Object[1];
        array[0] = new Object[] { array };
        Assert.assertEquals("[[{\"$ref\":\"..\"}]]", JSON.toJSONString(array));
    }
    
    public void test_parse() throws Exception {
        Object[] array2 = JSON.parseObject("[[{\"$ref\":\"..\"}]]", Object[].class);
        JSONArray item = (JSONArray) array2[0];
        Assert.assertSame(item, item.get(0));
    }
    
}
