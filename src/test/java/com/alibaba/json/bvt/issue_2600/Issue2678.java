package com.alibaba.json.bvt.issue_2600;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue2678 extends TestCase {
    public void test_field() throws Exception {
        Person person = new Person();
        person.setName("Ariston");
        person.setAge(23);
        String json = JSON.toJSONString(person);
        assertEquals("{\"age\":23,'name':'Ariston'}", json);
    }

    public void test_getter() throws Exception {
        Person2 person = new Person2();
        person.setName("Ariston");
        person.setAge(23);
        String json = JSON.toJSONString(person);
        assertEquals("{\"age\":23,'name':'Ariston'}", json);
    }

    static class Person {

        @JSONField(serialzeFeatures = SerializerFeature.UseSingleQuotes)
        private String name;

        private int age;

        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge( int age )
        {
            this.age = age;
        }
    }

    static class Person2 {

        private String name;

        private int age;

        @JSONField(serialzeFeatures = SerializerFeature.UseSingleQuotes)
        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge( int age )
        {
            this.age = age;
        }
    }
}
