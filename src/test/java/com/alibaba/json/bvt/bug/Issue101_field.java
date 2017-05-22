package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue101_field extends TestCase {

    public void test_for_issure() throws Exception {
        VO vo = new VO();
        vo.a = new Object();
        vo.b = vo.a;
        vo.c = vo.a;
        
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"a\":{},\"b\":{},\"c\":{\"$ref\":\"$.b\"}}", text);
    }

    public static class VO {

        private Object a;
        private Object b;
        private Object c;

        public Object getA() {
            return a;
        }

        public void setA(Object a) {
            this.a = a;
        }

        @JSONField(serialzeFeatures=SerializerFeature.DisableCircularReferenceDetect)
        public Object getB() {
            return b;
        }

        public void setB(Object b) {
            this.b = b;
        }

        public Object getC() {
            return c;
        }

        public void setC(Object c) {
            this.c = c;
        }

    }
}
