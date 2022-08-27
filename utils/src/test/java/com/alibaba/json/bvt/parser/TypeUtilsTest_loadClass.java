package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.util.TypeUtils;

import junit.framework.TestCase;

public class TypeUtilsTest_loadClass extends TestCase {

    public void test_loadClass() throws Exception {
        Assert.assertSame(Entity.class,
                          TypeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass$Entity",
                                              Entity.class.getClassLoader()));

        Assert.assertSame(Entity.class,
                          TypeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass$Entity", null));
    }

    public void test_error() throws Exception {
        Assert.assertNull(TypeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass.Entity",
                                              Entity.class.getClassLoader()));
    }

    public static class Entity {

    }
}
