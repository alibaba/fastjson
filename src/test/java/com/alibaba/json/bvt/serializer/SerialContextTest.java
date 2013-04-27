package com.alibaba.json.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerialContext;


public class SerialContextTest extends TestCase {
    public void test_context() throws Exception {
        SerialContext root = new SerialContext(null, null, null);
        SerialContext context = new SerialContext(root, null, "x");
        Assert.assertEquals("x", context.getFieldName());
        Assert.assertEquals("$.x", context.toString());
    }
}
