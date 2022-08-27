package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.util.Assert;

public class ClassConstructorTest1 extends TestCase {
    public void test_error() throws Exception {
        String modelJSON = "{\"age\":12, \"name\":\"nanqi\"}";
        Model model = JSON.parseObject(modelJSON, Model.class);
//        Assert.notNull(model.getName());
        //skip
    }

    public static class Model {
        public Model(int age) {
            this.age = age;
        }

        private String name;

        private int age;

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
