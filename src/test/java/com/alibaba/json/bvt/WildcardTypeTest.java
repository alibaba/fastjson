package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

public class WildcardTypeTest extends TestCase {
    public void test_for_wildcardType() throws Exception {
        TestType<B> b = new TestType<B>();
        b.value = new B(2001, "BBBB");
        b.superType = new TestType<A>(new A(101));

        String json = JSON.toJSONString(b);

        assertEquals("{\"superType\":{\"value\":{\"id\":101}},\"value\":{\"id\":2001,\"name\":\"BBBB\"}}", json);

        TestType<B> b1 = JSON.parseObject(json, new TypeReference<TestType<B>>() {});
        String json2 = JSON.toJSONString(b1);

        assertEquals(json, json2);
    }

    public static class TestType<X> {
        public X value;
        public TestType<? super X> superType;

        public TestType() {

        }

        public TestType(X value) {
            this.value = value;
        }
    }

    public static class TestType2<X, Y> {
        TestType2<? super Y, ? super X> superReversedType;
    }

    public static class A {
        public int id;

        public A(int id) {
            this.id = id;
        }
    }

    public static class B extends A {
        public String name;

        public B(int id, String name) {
            super(id);
            this.name = name;
        }
    }
}
