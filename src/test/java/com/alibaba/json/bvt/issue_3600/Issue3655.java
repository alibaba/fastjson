package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue3655 extends TestCase {

    public void test1() {
        B b = new B(null);
        System.out.println(JSON.toJSONString(b, SerializerFeature.WriteNullStringAsEmpty));
    }

    public static abstract class A {
        public abstract Object getData();
    }

    public static class B extends A {
        private String data;

        public B(String data) {
            this.data = data;
        }

        public String getData() {
            return this.data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
