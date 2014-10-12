package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerialContext;


public class SerialContextTest extends TestCase {
    public void test_context() throws Exception {
        SerialContext root = new SerialContext(null, null, null, 0, 0);
        SerialContext context = new SerialContext(root, null, "x", 0, 0);
        Assert.assertEquals("x", context.getFieldName());
        Assert.assertEquals("$.x", context.toString());
    }
}
