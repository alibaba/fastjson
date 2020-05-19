package com.alibaba.json.bvt.bug;

import java.io.Serializable;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_leupom_3 extends TestCase {

    public void test_bug() throws Exception {
        Person person = new Person();
        person.setId(12345);

        String text = JSON.toJSONString(person);
        
        System.out.println(text);
        
        Person person2 = JSON.parseObject(text, Person.class);
        
        Assert.assertEquals(person.getId(), person2.getId());
    }

    public abstract static interface Model {

        Serializable getId();
        void setId(Integer value);
    }

    public static class Person implements Model {

        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

    }
}
