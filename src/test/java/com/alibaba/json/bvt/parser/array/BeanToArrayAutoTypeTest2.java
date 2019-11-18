package com.alibaba.json.bvt.parser.array;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class BeanToArrayAutoTypeTest2
        extends TestCase {
    public void test_for_issue() throws Exception {
        Model m = new Model();
        m.value = new B(1001, "chengchao");
        String json = JSON.toJSONString(m);
        assertEquals("{\"value\":[\"@type\":\"B\",\"chengchao\",1001]}", json);

        Model m1 = JSON.parseObject(json, Model.class, Feature.SupportAutoType);
        assertEquals(m.value.getClass(), m1.value.getClass());
        assertEquals(json, JSON.toJSONString(m1));
    }

    public void test_for_issue_1() throws Exception {
        Model m = new Model();
        m.value = new C(1001, 58);
        String json = JSON.toJSONString(m);
        assertEquals("{\"value\":[\"@type\":\"C\",58,1001]}", json);

        Model m1 = JSON.parseObject(json, Model.class, Feature.SupportAutoType);
        assertEquals(m.value.getClass(), m1.value.getClass());
        assertEquals(json, JSON.toJSONString(m1));
    }

    @JSONType(seeAlso = {B.class, C.class}
            , serialzeFeatures = {SerializerFeature.BeanToArray, SerializerFeature.WriteClassName}
            , parseFeatures = Feature.SupportArrayToBean)
    public static class A {
        public int id;
    }

    @JSONType(typeName = "B", orders = {"name", "id"}
            , serialzeFeatures = {SerializerFeature.BeanToArray, SerializerFeature.WriteClassName}
            , parseFeatures = Feature.SupportArrayToBean)
    public static class B extends A {
        public String name;

        public B() {

        }

        public B(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @JSONType(typeName = "C", orders = {"age", "id"}
            , serialzeFeatures = {SerializerFeature.BeanToArray, SerializerFeature.WriteClassName}
            , parseFeatures = Feature.SupportArrayToBean)
    public static class C extends A {
        public int age;

        public C() {

        }

        public C(int id, int age) {
            this.id = id;
            this.age = age;
        }
    }

    public static class Model {
        public A value;
    }
}
