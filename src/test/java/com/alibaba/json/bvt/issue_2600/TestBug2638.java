package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

public class TestBug2638 {
    @Test
    public void canGiveJSONOnlyRBRACE() {
        Exception error = null;
        try {
            String str = "}";
            //String str="{\"age\":24,\"name\":\"李四\"}";
            JSON.parseObject(str, Person.class);
        }
        catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public static class Person implements Serializable {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            super();
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}