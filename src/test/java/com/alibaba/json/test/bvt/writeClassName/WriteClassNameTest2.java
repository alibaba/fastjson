package com.alibaba.json.test.bvt.writeClassName;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteClassNameTest2 extends TestCase {

    public void test_list() throws Exception {
        A a = new A();
        a.setB(new B());
        String text = JSON.toJSONString(a, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.test.bvt.writeClassName.WriteClassNameTest2$A\",\"b\":{\"id\":0}}",
                            text);

        A a1 = (A) JSON.parse(text);

        Assert.assertNotNull(a1.getB());
    }

    public static class A {

        private B b;

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }

    }

    public static final class B {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
