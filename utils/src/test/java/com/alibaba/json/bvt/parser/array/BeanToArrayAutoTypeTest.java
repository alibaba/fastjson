package com.alibaba.json.bvt.parser.array;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class BeanToArrayAutoTypeTest extends TestCase {
    public void test_for_issue_x() throws Exception {
        String json = "[\"@type\":\"B\",\"chengchao\",1001]";
        A a = JSON.parseObject(json, A.class, Feature.SupportAutoType, Feature.SupportArrayToBean);
        B b = (B) a;
    }

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

    @JSONType(seeAlso = {B.class, C.class})
    public static class A {
        protected int id;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }
    }

    @JSONType(typeName = "B", orders = {"name", "id"})
    public static class B extends A {
        private String name;

        public B() {

        }

        public B(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    @JSONType(typeName = "C", orders = {"age", "id"})
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
        @JSONField(serialzeFeatures = {SerializerFeature.BeanToArray, SerializerFeature.WriteClassName}
                , parseFeatures = Feature.SupportArrayToBean)
        public A value;
    }
}
