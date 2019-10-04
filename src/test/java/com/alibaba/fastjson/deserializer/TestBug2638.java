package com.alibaba.fastjson.deserializer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

class Person implements Serializable {
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

public class TestBug2638 {
    @Test
    public void canGiveJSONOnlyRBRACE(){
        try{
            String str="}";
            //String str="{\"age\":24,\"name\":\"李四\"}";
            Person person= JSON.parseObject(str, Person.class);
            fail("No exception thrown.");
        }catch(JSONException ex){
            //assertTrue(JSONException);
            assertTrue(ex.getMessage().equals("syntax error, expect {, actual }, pos 0, fastjson-version 1.2.60"));
        }
    }

}
