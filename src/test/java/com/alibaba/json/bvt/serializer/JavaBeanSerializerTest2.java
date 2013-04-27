package com.alibaba.json.bvt.serializer;

import java.util.Collections;

import junit.framework.TestCase;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;

public class JavaBeanSerializerTest2 extends TestCase {

    public void test_0() throws Exception {
        new JavaBeanSerializer(A.class, Collections.<String, String> emptyMap());
    }

    public static class A {

        @JSONField(name = "uid")
        private int     id;
        private String  name;

        @JSONField(deserialize = false)
        private boolean b1;

        @JSONField(name = "B2")
        private boolean b2;

        private byte[]  bytes;

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public boolean isB2() {
            return b2;
        }

        public void setB2(boolean b2) {
            this.b2 = b2;
        }

        public boolean isB1() {
            return b1;
        }

        public void setB1(boolean b1) {
            this.b1 = b1;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @JSONField(name = "xname")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
