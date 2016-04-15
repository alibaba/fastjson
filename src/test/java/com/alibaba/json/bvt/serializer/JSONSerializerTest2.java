package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IdentityHashMap;

import junit.framework.TestCase;

public class JSONSerializerTest2 extends TestCase {

    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        
        Field field = SerializeConfig.class.getDeclaredField("serializers");
        field.setAccessible(true);
        IdentityHashMap map = (IdentityHashMap) field.get(serializer.config);

        int size = size(map);
        serializer.out.config(SerializerFeature.WriteEnumUsingToString, false);
        serializer.write(Type.A);

        Assert.assertTrue(size < size(map));

        Assert.assertEquals(Integer.toString(Type.A.ordinal()), serializer.out.toString());
    }
    
    static int size(IdentityHashMap map) throws Exception {
        Field bucketsField = IdentityHashMap.class.getDeclaredField("buckets");
        bucketsField.setAccessible(true);
        Object[] buckets = (Object[]) bucketsField.get(map);
        int size = 0;
        
        Field nextField = Class.forName("com.alibaba.fastjson.util.IdentityHashMap$Entry").getDeclaredField("next");
        for (int i = 0; i < buckets.length; ++i) {
            for (Object entry = buckets[i]; entry != null; entry = nextField.get(entry)) {
                size++;
            }
        }
        return size;
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
