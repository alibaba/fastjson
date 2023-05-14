package com.alibaba.json.test.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;

/**
 * Created by wenshao on 02/04/2017.
 */
public class JacksonTypeInfoTest extends TestCase {
    public void test_typeinfo() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        A a = new B();

        String str = mapper.writeValueAsString(a);
        System.out.println(str);

        Object x = mapper.readValue(str, A.class);
        System.out.println(x.getClass());
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public static class A {
        protected int a;

        public A() {
        }

        public A(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }
    }

    public static class B extends A {

    }
}
