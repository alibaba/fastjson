package com.alibaba.json.bvt.annotation;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

public class JSONTypejsonType_alphabetic_Test extends TestCase {

    public void test_alphabetic_true() throws Exception {
        A a = new A();
        a.setF0(101);
        a.setF1(102);

        Assert.assertEquals("{\"f0\":101,\"f1\":102}", JSON.toJSONString(a));
    }

//    public void test_alphabetic_false() throws Exception {
//        B b = new B();
//        b.setF0(101);
//        b.setF1(102);
//
//        Assert.assertFalse("{\"f2\":0,\"f1\":102,\"f0\":101}".equals(JSON.toJSONString(b)));
//    }

    public void test_alphabetic_notSet() throws Exception {
        C c = new C();
        c.setF0(101);
        c.setF1(102);

        Assert.assertEquals("{\"f0\":101,\"f1\":102}", JSON.toJSONString(c));
    }

    @JSONType(alphabetic = true)
    public static class A {

        private int f1;
        private int f0;

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

    }

    @JSONType(alphabetic = false)
    public static class B {

        private int f2;
        private int f1;
        private int f0;

        public int getF2() {
            return f2;
        }

        public void setF2(int f2) {
            this.f2 = f2;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

    }

    public static class C {

        private int f1;
        private int f0;

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

    }
}
