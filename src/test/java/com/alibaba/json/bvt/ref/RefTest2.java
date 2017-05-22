package com.alibaba.json.bvt.ref;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest2 extends TestCase {

    public void test_ref() throws Exception {
        Object[] array = new Object[1];
        array[0] = array;
        Assert.assertEquals("[{\"$ref\":\"@\"}]", JSON.toJSONString(array));
    }

    public void test_ref_1() throws Exception {
        Object[] array = new Object[3];
        array[0] = array;
        array[1] = new Object();
        array[2] = new Object();
        Assert.assertEquals("[{\"$ref\":\"@\"},{},{}]", JSON.toJSONString(array));
    }

    public void test_ref_2() throws Exception {
        Object[] array = new Object[3];
        array[0] = new Object();
        array[1] = array;
        array[2] = new Object();
        Assert.assertEquals("[{},{\"$ref\":\"@\"},{}]", JSON.toJSONString(array));
    }

    public void test_ref_3() throws Exception {
        Object[] array = new Object[3];
        array[0] = new Object();
        array[1] = new Object();
        array[2] = array;
        Assert.assertEquals("[{},{},{\"$ref\":\"@\"}]", JSON.toJSONString(array));
    }

    public void test_parse() throws Exception {
        Object[] array2 = JSON.parseObject("[{\"$ref\":\"$\"}]", Object[].class);
        Assert.assertSame(array2, array2[0]);
    }

    public void test_parse_1() throws Exception {
        Object[] array2 = JSON.parseObject("[{\"$ref\":\"@\"}]", Object[].class);
        Assert.assertSame(array2, array2[0]);
    }
}
