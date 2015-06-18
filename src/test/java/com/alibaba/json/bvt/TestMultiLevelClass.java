package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class TestMultiLevelClass extends TestCase {

    public static class A {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

        public static class B {

            private C c;

            public C getC() {
                return c;
            }

            public void setC(C c) {
                this.c = c;
            }

            static class C {

                private int value;

                
                public int getValue() {
                    return value;
                }

                
                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }

    public void test_codec() throws Exception {
        A a = new A();
        a.setB(new A.B());
        a.getB().setC(new A.B.C());
        a.getB().getC().setValue(123);
        
        String text = JSON.toJSONString(a);
        System.out.println(text);
        
        A a2 = JSON.parseObject(text, A.class);
    }
}
