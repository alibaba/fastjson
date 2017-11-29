package com.alibaba.json.bvt.ref;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest7 extends TestCase {

    public void test_bug_for_juqkai() throws Exception {

        VO vo = new VO();
        C c = new C();
        vo.setA(new A(c));
        vo.setB(new B(c));

        VO[] root = new VO[] { vo };

        String text = JSON.toJSONString(root);
        System.out.println(text);

        VO[] array2 = JSON.parseObject(text, VO[].class);
        Assert.assertEquals(1, array2.length);
        Assert.assertNotNull(array2[0].getA());
        Assert.assertNotNull(array2[0].getB());
        Assert.assertNotNull(array2[0].getA().getC());
        Assert.assertNotNull(array2[0].getB().getC());
        Assert.assertSame(array2[0].getA().getC(), array2[0].getB().getC());
    }

    public static class VO {

        private A a;
        private B b;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

    }

    public static class A {

        private C c;

        public A(){

        }

        public A(C c){
            this.c = c;
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }

    }

    public static class B {

        private C c;

        public B(){

        }

        public B(C c){
            this.c = c;
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }
    }

    public static class C {

        public C(){

        }

    }
}
