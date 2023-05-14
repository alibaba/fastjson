package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BooleanArraySerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
    }

    public static class Entity {

        private boolean[] value;

        public boolean[] getValue() {
            return value;
        }

        public void setValue(boolean[] value) {
            this.value = value;
        }

    }
}
