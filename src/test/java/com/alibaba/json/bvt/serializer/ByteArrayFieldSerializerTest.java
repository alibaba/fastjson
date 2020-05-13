package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ByteArrayFieldSerializerTest extends TestCase {

    public void test_0() throws Exception {
        A a1 = new A();
        a1.setBytes(new byte[] { 1, 2 });

        Assert.assertEquals("{\"bytes\":\"AQI=\"}", JSON.toJSONString(a1));
    }

    public void test_1() throws Exception {
        A a1 = new A();

        Assert.assertEquals("{\"bytes\":null}", JSON.toJSONString(a1, SerializerFeature.WriteMapNullValue));
    }

    public static class A {

        private byte[] bytes;

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

    }
}
