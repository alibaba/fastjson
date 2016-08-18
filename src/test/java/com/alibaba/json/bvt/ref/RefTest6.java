package com.alibaba.json.bvt.ref;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RefTest6 extends TestCase {

    /**
     * A -> B -> C -> B -> A
     * 
     * @throws Exception
     */
    public void test_0() throws Exception {
        A a = new A();
        B b = new B();
        C c = new C();
        a.setB(b);
        b.setC(c);
        c.setB(b);
        b.setA(a);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", a);
        jsonObject.put("c", c);

        String text = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
        System.out.println(text);
    }

    private class A {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }

    private class B {

        private C c;
        private A a;

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }
    }

    private class C {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }

}
