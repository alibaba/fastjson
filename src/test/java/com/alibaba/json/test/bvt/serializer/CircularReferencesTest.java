package com.alibaba.json.test.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CircularReferencesTest extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        JSONException error = null;
        try {
            JSON.toJSONString(a);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_1() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        JSONException error = null;
        try {
            JSON.toJSONString(a, SerializerFeature.UseISO8601DateFormat);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_2() throws Exception {
        A a = new A();
        B b = new B(a);
        a.setB(b);

        JSONException error = null;
        try {
            JSON.toJSONString(a, true);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
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

        public B(A a){
            this.a = a;
        }

        public A getA() {
            return a;
        }

    }
}
