package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestEnum extends TestCase {

    public static enum Type {
        Big, Medium, Small
    }

    public void test_enum() throws Exception {
        Assert.assertEquals(Type.Big, JSON.parseObject("{value:\"Big\"}", VO.class).getValue());
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"value\":\"Big\"}", VO.class).getValue());
        Assert.assertEquals(Type.Big, JSON.parseObject("{value:0}", VO.class).getValue());
        Assert.assertEquals(Type.Big, JSON.parseObject("{\"value\":0}", VO.class).getValue());
    }

    public static class VO {

        private Type value;

        public Type getValue() {
            return value;
        }

        public void setValue(Type value) {
            this.value = value;
        }

    }
}
