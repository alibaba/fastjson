package com.alibaba.json.bvt.parser.deser.list;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ArrayDeserializerTest extends TestCase {

    public void test_null() throws Exception {
        Assert.assertNull(JSON.parseObject("null", Object[].class));
        Assert.assertNull(JSON.parseObject("null", String[].class));
        Assert.assertNull(JSON.parseObject("null", VO[].class));
        Assert.assertNull(JSON.parseObject("null", VO[][].class));
    }

    public void test_0() throws Exception {
        Assert.assertEquals(0, JSON.parseObject("[]", Object[].class).length);
        Assert.assertEquals(0, JSON.parseObject("[]", Object[][].class).length);
        Assert.assertEquals(0, JSON.parseObject("[]", Object[][][].class).length);
        Assert.assertEquals(1, JSON.parseObject("[null]", Object[].class).length);
        Assert.assertEquals(1, JSON.parseObject("[null]", Object[][].class).length);
        Assert.assertEquals(1, JSON.parseObject("[[[[[[]]]]]]", Object[][].class).length);
        Assert.assertEquals(1, JSON.parseObject("[null]", Object[][][].class).length);
        Assert.assertEquals(null, JSON.parseObject("{\"value\":null}", VO.class).getValue());
    }

    public static class VO {

        private Object[] value;

        public Object[] getValue() {
            return value;
        }

        public void setValue(Object[] value) {
            this.value = value;
        }

    }
}
