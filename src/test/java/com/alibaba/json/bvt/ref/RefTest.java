package com.alibaba.json.bvt.ref;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RefTest extends TestCase {
    
    public void test_ref() throws Exception {
        JSONSerializer ser = new JSONSerializer();
        Assert.assertFalse(ser.containsReference(null));
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
