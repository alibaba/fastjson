package com.alibaba.json.bvt.bug;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_leupom extends TestCase {

    public void test_bug() throws Exception {
        Person person = new Person();
        person.setId(12345);
        
        String text = JSON.toJSONString(person);
        
        System.out.println(text);
    }

    public abstract static class Model {

        public abstract Serializable getId();

    }

    public static class Person extends Model {

        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
}
