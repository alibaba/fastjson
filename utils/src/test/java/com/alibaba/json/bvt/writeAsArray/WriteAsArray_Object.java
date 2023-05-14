package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.writeAsArray.WriteAsArray_enum_public.VO;

public class WriteAsArray_Object extends TestCase {

    public void test_0() throws Exception {
        A a = new A();
        a.setId(123);
        a.setName("wenshao");
        
        VO vo = new VO();
        vo.setA(a);

        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[[123,\"wenshao\"]]", text);
        
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo.getA().getId(), vo2.getA().getId());
        Assert.assertEquals(vo.getA().getName(), vo2.getA().getName());
    }

    private static class VO {

        private A a;

        public A getA() {
            return a;
        }

        public void setA(A a) {
            this.a = a;
        }

    }

    private static class A {

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
