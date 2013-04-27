package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteClassNameTest extends TestCase {

    public void test_writeClassName() throws Exception {
        Entity object = new Entity();
        object.setId(123);
        object.setName("jobs");
        object.setAverage(3.21F);
        
        SerializeConfig config = new SerializeConfig();
        config.setAsmEnable(false);
        String text = JSON.toJSONString(object, config, SerializerFeature.WriteClassName);
        System.out.println(text);
    }

    public static class Entity {

        private int    id;
        private String name;
        private float  average;

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

        public float getAverage() {
            return average;
        }

        public void setAverage(float average) {
            this.average = average;
        }

    }
}
