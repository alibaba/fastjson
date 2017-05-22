package com.alibaba.json.bvt.typeRef;

import java.util.LinkedHashMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class TypeReferenceTest5 extends TestCase {

    public void test_typeRef() throws Exception {
        TypeReference<A<B>> typeRef = new TypeReference<A<B>>() {
        };

        A<B> a = JSON.parseObject("{\"body\":{\"id\":123}}", typeRef);
        
        B b = a.getBody();
        Assert.assertEquals(123, b.get("id"));
    }

    public static class A<T> {

        private T body;

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }

    }

    public static class B extends LinkedHashMap<String, Object> {

        private static final long serialVersionUID = 1L;

    }
}
