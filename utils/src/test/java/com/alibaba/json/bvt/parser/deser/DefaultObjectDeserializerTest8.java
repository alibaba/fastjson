package com.alibaba.json.bvt.parser.deser;

import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class DefaultObjectDeserializerTest8 extends TestCase {

    public <T> void test_1() throws Exception {
        VO<T> vo = JSON.parseObject("{\"value\":[{\"id\":123}]}", new TypeReference<VO<T>>() {
        });
        Assert.assertNotNull(vo.getValue()[0]);
        Assert.assertTrue(vo.getValue()[0] instanceof Map);
    }

    public static class VO<T> {

        private T[] value;

        public T[] getValue() {
            return value;
        }

        public void setValue(T[] value) {
            this.value = value;
        }

    }

    public static class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
