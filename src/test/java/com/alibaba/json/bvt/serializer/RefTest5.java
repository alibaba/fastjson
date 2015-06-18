package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest5 extends TestCase {
    
    public void test_ref() throws Exception {
        Object[] array = new Object[1];
        array[0] = new Object[] { array };
        Assert.assertEquals("[[{\"$ref\":\"..\"}]]", JSON.toJSONString(array));
    }
    
    public void test_parse() throws Exception {
        Object[] array2 = JSON.parseObject("[[{\"$ref\":\"..\"}]]", Object[].class);
        Object[] item = (Object[]) array2[0];
        Assert.assertSame(array2, item[0]);
    }
    
}
