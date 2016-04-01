package com.alibaba.json.bvt.serializer;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao
 */
public class PropertyPathTest extends TestCase {

    public void test_path() throws Exception {
        A a = new A();
        a.setId(123);

        B b = new B();
        b.setId(234);

        C c = new C();
        c.setId(345);

        D d = new D();
        d.setId(456);

        a.setB(b);
        b.setC(c);
        b.setD(d);

        Assert.assertEquals("{\"b\":{\"c\":{\"id\":345},\"d\":{\"id\":456},\"id\":234},\"id\":123}",
                            JSON.toJSONString(a));
        Assert.assertEquals("{\"b\":{\"c\":{\"id\":345},\"id\":234},\"id\":123}",
                            JSON.toJSONString(a, new MyPropertyPreFilter()));
        Assert.assertEquals("{'b':{'c':{'id':345},'id':234},'id':123}",
                            JSON.toJSONString(a, new MyPropertyPreFilter(), SerializerFeature.UseSingleQuotes));
    }

    public static class MyPropertyPreFilter implements PropertyPreFilter {

        public boolean apply(JSONSerializer serializer, Object source, String name) {
            String path = serializer.getContext().toString() + "." + name;

            if (path.startsWith("$.b.d")) {
                return false;
            }

            return true;
        }

    }

    public static class A {

        private int id;
        private B   b;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

    }

    public static class B {

        private int id;
        private C   c;
        private D   d;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }

        public D getD() {
            return d;
        }

        public void setD(D d) {
            this.d = d;
        }

    }

    public static class C {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class D {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
