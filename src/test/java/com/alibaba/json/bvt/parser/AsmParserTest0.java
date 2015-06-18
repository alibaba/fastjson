package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class AsmParserTest0 extends TestCase {

    public void test_asm() throws Exception {
        A a = JSON.parseObject("{\"f1\":123}", A.class);
        Assert.assertNotNull(a.getF2());
    }

    public static class A {

        private int f1;

        private B   f2 = new B();

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public B getF2() {
            return f2;
        }

        public void setF2(B f2) {
            this.f2 = f2;
        }

    }

    public static class B {

    }
}
