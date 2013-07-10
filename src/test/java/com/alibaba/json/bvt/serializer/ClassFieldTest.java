package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class ClassFieldTest extends TestCase {

    public void test_writer_1() throws Exception {
        VO vo = JSON.parseObject("{\"value\":\"int\"}", VO.class);
        Assert.assertEquals(int.class, vo.getValue());
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
