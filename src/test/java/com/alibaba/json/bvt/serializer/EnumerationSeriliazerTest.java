package com.alibaba.json.bvt.serializer;

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EnumerationSeriliazerTest extends TestCase {

    public void test_nullAsEmtpyList() throws Exception {
        VO e = new VO();
        Assert.assertEquals("{\"elements\":[]}", JSON.toJSONString(e, SerializerFeature.WriteMapNullValue,
                                                                   SerializerFeature.WriteNullListAsEmpty));
    }

    public void test_null() throws Exception {
        VO e = new VO();
        Assert.assertEquals("{\"elements\":null}", JSON.toJSONString(e, SerializerFeature.WriteMapNullValue));
    }

    public void test_1() throws Exception {
        VO e = new VO(new Entity(), new Entity());
        Assert.assertEquals("{\"elements\":[{},{}]}", JSON.toJSONString(e, SerializerFeature.WriteMapNullValue));
    }

    public void test_2() throws Exception {
        VO e = new VO(new Entity(), new Entity2());
        Assert.assertEquals("{\"elements\":[{},{\"@type\":\"com.alibaba.json.bvt.serializer.EnumerationSeriliazerTest$Entity2\"}]}",
                            JSON.toJSONString(e, SerializerFeature.WriteClassName,
                                              SerializerFeature.NotWriteRootClassName));
    }

    public void test_3() throws Exception {
        VO2 e = new VO2(new Entity(), new Entity());
        Assert.assertEquals("{\"elements\":[{},{}]}", JSON.toJSONString(e, SerializerFeature.WriteClassName,
                                                                        SerializerFeature.NotWriteRootClassName));
    }
    
    public void test_4() throws Exception {
        VO3 e = new VO3(new Entity(), new Entity2());
        Assert.assertEquals("{\"elements\":[{\"@type\":\"com.alibaba.json.bvt.serializer.EnumerationSeriliazerTest$Entity\"},{\"@type\":\"com.alibaba.json.bvt.serializer.EnumerationSeriliazerTest$Entity2\"}]}", JSON.toJSONString(e, SerializerFeature.WriteClassName,
                                                                        SerializerFeature.NotWriteRootClassName));
    }

    private static class VO {

        private Enumeration<Entity> elements;

        public VO(Entity... array){
            if (array.length > 0) {
                Vector<Entity> vector = new Vector<Entity>();
                for (Entity item : array) {
                    vector.add(item);
                }
                this.elements = vector.elements();
            }
        }

        public Enumeration<Entity> getElements() {
            return elements;
        }

        public void setElements(Enumeration<Entity> elements) {
            this.elements = elements;
        }

    }

    private static class VO2 extends IVO2<Entity> {

        public VO2(Entity... array){
            if (array.length > 0) {
                Vector<Entity> vector = new Vector<Entity>();
                for (Entity item : array) {
                    vector.add(item);
                }
                this.elements = vector.elements();
            }
        }

    }
    
    private static class VO3 {

        private Enumeration elements;

        public VO3(Entity... array){
            if (array.length > 0) {
                Vector<Entity> vector = new Vector<Entity>();
                for (Entity item : array) {
                    vector.add(item);
                }
                this.elements = vector.elements();
            }
        }

        public Enumeration getElements() {
            return elements;
        }

        public void setElements(Enumeration elements) {
            this.elements = elements;
        }

    }

    private static abstract class IVO2<T> {

        protected Enumeration<Entity> elements;

        public Enumeration<Entity> getElements() {
            return elements;
        }

        public void setElements(Enumeration<Entity> elements) {
            this.elements = elements;
        }
    }

    public static class Entity {

    }

    public static class Entity2 extends Entity {

    }
}
