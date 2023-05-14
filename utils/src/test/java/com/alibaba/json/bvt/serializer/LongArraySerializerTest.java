package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class LongArraySerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("[]", JSON.toJSONString(new long[0]));
        Assert.assertEquals("[1,2]", JSON.toJSONString(new long[] { 1, 2 }));
        Assert.assertEquals("[1,2,3,-4]", JSON.toJSONString(new long[] { 1, 2, 3, -4 }));
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
    }

    public static class Entity {

        private long[] value;

        public long[] getValue() {
            return value;
        }

        public void setValue(long[] value) {
            this.value = value;
        }

    }
}
