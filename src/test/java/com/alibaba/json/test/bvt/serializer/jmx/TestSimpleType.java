package com.alibaba.json.test.bvt.serializer.jmx;

import javax.management.openmbean.SimpleType;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestSimpleType extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("\"java.lang.Boolean\"", JSON.toJSONString(SimpleType.BOOLEAN));
    }
}
