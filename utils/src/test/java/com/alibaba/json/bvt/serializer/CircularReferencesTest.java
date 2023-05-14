package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CircularReferencesTest extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        String text = JSON.toJSONString(a);
        A a1 = JSON.parseObject(text, A.class);
        Assert.assertTrue(a1 == a1.getB().getA());
    }

    public void test_1() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        String text = JSON.toJSONString(a, SerializerFeature.UseISO8601DateFormat);
        A a1 = JSON.parseObject(text, A.class);
        Assert.assertTrue(a1 == a1.getB().getA());
    }

    public void test_2() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        String text = JSON.toJSONString(a, true);
        A a1 = JSON.parseObject(text, A.class);
        Assert.assertTrue(a1 == a1.getB().getA());
    }

    public static class A {

        private B b;

        public A(){
        }

        public A(B b){
            this.b = b;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

    }

    public static class B {

        private A a;

        public B(){

        }

        public B(A a){
            this.a = a;
        }

        public A getA() {
            return a;
        }
        
        public void setA(A a) {
            this.a = a;
        }
    }
}
