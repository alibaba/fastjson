package com.alibaba.json.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ClassTest extends TestCase {

    public void test_class() throws Exception {
        Assert.assertNull(JSON.parseObject("null", Class.class));
        Assert.assertNull(JSON.parseObject("{value:null}", VO.class).getValue());
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
