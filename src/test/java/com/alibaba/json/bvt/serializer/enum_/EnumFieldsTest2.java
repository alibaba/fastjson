package com.alibaba.json.bvt.serializer.enum_;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class EnumFieldsTest2 extends TestCase {

    public void test_enum() throws Exception {
        Model model = new Model();
        model.t1 = Type.A;
        model.t2 = null;

        String text = JSON.toJSONString(model, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"t1\":\"A\",\"t2\":null}", text);
    }

    public static class Model {

        private Type t1;
        private Type t2;

        public Type getT1() {
            return t1;
        }

        public void setT1(Type t1) {
            this.t1 = t1;
        }

        public Type getT2() {
            return t2;
        }

        public void setT2(Type t2) {
            this.t2 = t2;
        }

    }

    private static enum Type {
                              A, B, C
    }
}
