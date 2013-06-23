package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializerContext;

public class JSONSerializerContextTest extends TestCase {

    public void test_0() throws Exception {
        JSONSerializerContext context = new JSONSerializerContext();

        int len = 1000 * 10;
        Object[] object = new Object[len];

        for (int i = 0; i < len; ++i) {
            object[i] = i;
        }

        for (int i = 0; i < len; ++i) {
            Assert.assertEquals(false, context.put(object[i]));
        }

        for (int i = 0; i < len; ++i) {
            Assert.assertEquals(true, context.put(object[i]));
        }
    }
}
