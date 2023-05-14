package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FloatArraySerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("[]", JSON.toJSONString(new float[0]));
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        Assert.assertEquals("[1.0,2.0]", JSON.toJSONString(new float[] { 1, 2 }));
        Assert.assertEquals("[1.0,2.0,3.0]", JSON.toJSONString(new float[] { 1, 2, 3 }));
        Assert.assertEquals("[1.0,2.0,3.0,null,null]", JSON.toJSONString(new float[] { 1, 2, 3, Float.NaN, Float.NaN }));
    }

    public static class Entity {

        private float[] value;

        public float[] getValue() {
            return value;
        }

        public void setValue(float[] value) {
            this.value = value;
        }

    }
}
