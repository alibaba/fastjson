package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_castToBytes extends TestCase {

    public void test_castToDate() throws Exception {
        Assert.assertArrayEquals(new byte[0], TypeUtils.castToBytes(new byte[0]));
    }

    public void test_castToDate_error() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToBytes(new int[0]);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
