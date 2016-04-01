package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.io.Writer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONSerializerTest2 extends TestCase {

    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        int size = serializer.config.size();
        serializer.out.config(SerializerFeature.WriteEnumUsingToString, false);
        serializer.write(Type.A);

        Assert.assertTrue(size < serializer.config.size());

        Assert.assertEquals(Integer.toString(Type.A.ordinal()), serializer.out.toString());
    }

    public void test_1() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.out.config(SerializerFeature.WriteEnumUsingToString, false);
        serializer.write(new A(Type.B));

        Assert.assertEquals("{\"type\":" + Integer.toString(Type.B.ordinal()) + "}", serializer.out.toString());

        A a = JSON.parseObject(serializer.out.toString(), A.class);
        Assert.assertEquals(a.getType(), Type.B);
    }

    public void test_2() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.write(new C());

        Assert.assertEquals("{}", serializer.out.toString());
    }

    public void test_3() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        serializer.out.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.write(new A(Type.B));

        Assert.assertEquals("{\"type\":\"B\"}", serializer.out.toString());

        A a = JSON.parseObject(serializer.out.toString(), A.class);
        Assert.assertEquals(a.getType(), Type.B);
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSONSerializer.write(new Writer() {

                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException();
                }

                @Override
                public void flush() throws IOException {
                    throw new IOException();
                }

                @Override
                public void close() throws IOException {
                    throw new IOException();
                }

            }, (Object) "abc");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static enum Type {
        A, B
    }

    public static class A {

        private Type type;

        public A(){

        }

        public A(Type type){
            super();
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

    }

    public static class C {

    }
}
