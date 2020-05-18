package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ClassTest extends TestCase {

    public void test_null() throws Exception {
        Assert.assertNull(JSON.parseObject("null", Class.class));
        Assert.assertNull(JSON.parseObject("null", Class[].class));
        Assert.assertNull(JSON.parseArray("null", Class.class));
        Assert.assertNull(JSON.parseObject("{value:null}", VO.class).getValue());
    }

    public void test_primitive() throws Exception {
        Assert.assertEquals(byte.class, JSON.parseObject("\"byte\"", Class.class));
        Assert.assertEquals(short.class, JSON.parseObject("\"short\"", Class.class));
        Assert.assertEquals(int.class, JSON.parseObject("\"int\"", Class.class));
        Assert.assertEquals(long.class, JSON.parseObject("\"long\"", Class.class));
        Assert.assertEquals(float.class, JSON.parseObject("\"float\"", Class.class));
        Assert.assertEquals(double.class, JSON.parseObject("\"double\"", Class.class));
        Assert.assertEquals(char.class, JSON.parseObject("\"char\"", Class.class));
        Assert.assertEquals(boolean.class, JSON.parseObject("\"boolean\"", Class.class));
    }

    public void test_array() throws Exception {
        Assert.assertEquals(int[].class, JSON.parseObject("\"[int\"", Class.class));
        Assert.assertEquals(int[][].class, JSON.parseObject("\"[[int\"", Class.class));
        Assert.assertEquals(int[][][][].class, JSON.parseObject("\"[[[[int\"", Class.class));
    }

    public static class VO {

        private Class<?> value;

        public Class<?> getValue() {
            return value;
        }

        public void setValue(Class<?> value) {
            this.value = value;
        }

    }
}
