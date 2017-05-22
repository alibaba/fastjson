package com.alibaba.json.bvt.writeAsArray;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteAsArray_Object_2_public extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        a.setId(123);
        a.setName("wenshao");

        VO vo = new VO();
        vo.setId(1001);
        vo.setValue(a);

        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[1001,[123,\"wenshao\"]]", text);

        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo.getValue().getId(), vo2.getValue().getId());
        Assert.assertEquals(vo.getValue().getName(), vo2.getValue().getName());
    }

    public static class VO {

        private int id;
        private A   value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public A getValue() {
            return value;
        }

        public void setValue(A value) {
            this.value = value;
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
