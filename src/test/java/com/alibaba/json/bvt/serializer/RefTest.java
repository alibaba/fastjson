package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class RefTest extends TestCase {
    
    public void test_ref() throws Exception {
        JSONSerializer ser = new JSONSerializer();
        
        Field field = JSONSerializer.class.getDeclaredField("references");
        field.setAccessible(true);
        IdentityHashMap<Object, SerialContext> map = (IdentityHashMap<Object, SerialContext>) field.get(ser);
        Assert.assertFalse(map != null && map.containsKey(null));
    }
    
    public void test_array_ref() throws Exception {
        JSON.toJSONString(new A[] {new A()}, SerializerFeature.DisableCircularReferenceDetect);
    }

    public class A {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }
}
