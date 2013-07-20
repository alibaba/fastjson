package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_Object extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        a.setId(123);
        a.setName("wenshao");
        
        VO vo = new VO();
        vo.setA(a);

        String text = JSON.toJSONString(vo, SerializerFeature.WriteJavaBeanAsArray);
        Assert.assertEquals("[[123,\"wenshao\"]]", text);
    }

    public static class VO {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }

    public static class A {

        private int    id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
