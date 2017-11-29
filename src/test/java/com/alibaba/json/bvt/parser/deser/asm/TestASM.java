package com.alibaba.json.bvt.parser.deser.asm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.test.benchmark.encode.EishayEncode;

public class TestASM extends TestCase {

    public void test_asm() throws Exception {
        String text = JSON.toJSONString(EishayEncode.mediaContent);
        System.out.println(text);
    }

    public void test_0() throws Exception {
        Department department = new Department();

        Person person = new Person();
        person.setId(123);
        person.setName("刘伟加");
        person.setAge(40);
        person.setSalary(new BigDecimal("123456"));
        person.getValues().add("A");
        person.getValues().add("B");
        person.getValues().add("C");

        department.getPersons().add(person);
        department.getPersons().add(new Person());
        department.getPersons().add(new Person());

        {
            String text = JSON.toJSONString(department);
            System.out.println(text);
        }
        {
            String text = JSON.toJSONString(department, SerializerFeature.WriteMapNullValue);
            System.out.println(text);
        }
    }

    public static class Person {

        private int          id;
        private String       name;
        private int          age;
        private BigDecimal   salary;

        private List<Person> childrens = new ArrayList<Person>();

        private List<String> values    = new ArrayList<String>();

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

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

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public List<Person> getChildrens() {
            return childrens;
        }

        public void setChildrens(List<Person> childrens) {
            this.childrens = childrens;
        }
    }

    public static class Department {

        private int          id;
        private String       name;
        private List<Person> persons = new ArrayList<Person>();

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

        public List<Person> getPersons() {
            return persons;
        }

        public void setPersons(List<Person> persons) {
            this.persons = persons;
        }

    }
}
