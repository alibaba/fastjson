package com.alibaba.json.bvt.ref;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class RefTest8 extends TestCase {

    public void test_bug_for_juqkai() throws Exception {

        C c = new C();
 
        Map<String, Object> a = Collections.<String,Object>singletonMap("c", c);
        Map<String, Object> b = Collections.<String,Object>singletonMap("c", c);
        Map<String, Object> vo = new HashMap<String, Object>();
        vo.put("a", a);
        vo.put("b", b);

        Object[] root = new Object[] { vo };

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

    private static class VO {

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

    private static class A {

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

    private static class B {

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

    private static class C {

        public C(){

        }

    }
}
