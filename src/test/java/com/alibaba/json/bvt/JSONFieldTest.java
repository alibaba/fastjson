package com.alibaba.json.bvt;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;


public class JSONFieldTest extends TestCase {
    
    public void test_field() throws Exception {
        Demo demo = new Demo();
        demo.setId(1009);
        demo.setName("IT");
        demo.setAge(30);
        System.out.println(JSON.toJSON(demo));
    }

    public static class Demo {
        private int id;

        @JSONField(serialize = false)
        private String name;

        private int age;

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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }


    }
}
