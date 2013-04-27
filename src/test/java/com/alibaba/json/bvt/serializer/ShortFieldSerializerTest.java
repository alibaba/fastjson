package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ShortFieldSerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{\"value\":0}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero));
    }

    public static class Entity {

        private Short value;

        public Short getValue() {
            return value;
        }

        public void setValue(Short value) {
            this.value = value;
        }

    }
}
