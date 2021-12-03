package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import edu.emory.mathcs.backport.java.util.Arrays;
import junit.framework.TestCase;
import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eliash0913 on 2021-11-07
 */

public class Issue3923 extends TestCase {
    final String peopleJSON = "[\n" +
            "    {\n" +
            "        \"name\": \"Elias\",\n" +
            "        \"age\": 25,\n" +
            "        \"height\": 180.1,\n" +
            "        \"weight\": 170.2\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Jeniffer\",\n" +
            "        \"age\": 23,\n" +
            "        \"height\": 167.2,\n" +
            "        \"weight\": 101.2\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Katy\",\n" +
            "        \"age\": 20,\n" +
            "        \"height\": 172.2,\n" +
            "        \"weight\": 98.4\n" +
            "    },\n" +
            "    {\n" +
            "        \"name\": \"Rachael\",\n" +
            "        \"age\": 21,\n" +
            "        \"height\": 171.2,\n" +
            "        \"weight\": 99.2\n" +
            "    }\n" +
            "]";

    public void test_floats() throws Exception {
        JSONArray ja = JSON.parseArray(Person.class,peopleJSON);
        Object[] people = {
                new Person("Elias",25,(float)180.1, (float)170.2),
                new Person("Jeniffer", 23, (float)167.2, (float)101.2),
                new Person("Katy", 20, (float)172.2, (float)98.4),
                new Person("Rachael", 21, (float)171.2, (float)99.2)};
        List<Person> expected = Arrays.asList(people);
        List<Person> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    public void test_bigDecimal() throws Exception {
        JSONArray ja = JSON.parseArray(Person2.class,peopleJSON);
        Object[] people = {
                new Person2("Elias",25,BigDecimal.valueOf(180.1), BigDecimal.valueOf(170.2)),
                new Person2("Jeniffer", 23, BigDecimal.valueOf(167.2),BigDecimal.valueOf(101.2)),
                new Person2("Katy", 20, BigDecimal.valueOf(172.2), BigDecimal.valueOf(98.4)),
                new Person2("Rachael", 21, BigDecimal.valueOf(171.2), BigDecimal.valueOf(99.2))};
        List<Person2> expected = Arrays.asList(people);
        List<Person2> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    public void test_int() throws Exception {
        JSONArray ja = JSON.parseArray(Person3.class,peopleJSON);
        Object[] people = {
                new Person3("Elias",25,(int)180.1, (int)170.2),
                new Person3("Jeniffer", 23, (int)167.2, (int)101.2),
                new Person3("Katy", 20, (int)172.2, (int)98.4),
                new Person3("Rachael", 21, (int)171.2, (int)99.2)};
        List<Person3> expected = Arrays.asList(people);
        List<Person3> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    public void test_double() throws Exception {
        JSONArray ja = JSON.parseArray(Person4.class,peopleJSON);
        Object[] people = {
                new Person4("Elias",25,180.1, 170.2),
                new Person4("Jeniffer", 23, 167.2, 101.2),
                new Person4("Katy", 20, 172.2, 98.4),
                new Person4("Rachael", 21, 171.2, 99.2)};
        List<Person4> expected = Arrays.asList(people);
        List<Person4> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    public void test_double_bigdecimal() throws Exception {
        JSONArray ja = JSON.parseArray(Person5.class,peopleJSON);
        Object[] people = {
                new Person5("Elias",25,180.1, BigDecimal.valueOf(170.2)),
                new Person5("Jeniffer", 23, 167.2, BigDecimal.valueOf(101.2)),
                new Person5("Katy", 20, 172.2, BigDecimal.valueOf(98.4)),
                new Person5("Rachael", 21, 171.2, BigDecimal.valueOf(99.2))};
        List<Person5> expected = Arrays.asList(people);
        List<Person5> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }


    public void test_byte_float() throws Exception {
        JSONArray ja = JSON.parseArray(Person6.class,peopleJSON);
        Object[] people = {
                new Person6("Elias",25,(byte)180.1, (float)170.2),
                new Person6("Jeniffer", 23, (byte)167.2, (float)101.2),
                new Person6("Katy", 20, (byte)172.2, (float)98.4),
                new Person6("Rachael", 21, (byte)171.2, (float)99.2)};
        List<Person6> expected = Arrays.asList(people);
        List<Person6> actual = ja.toJavaList();
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }

    @Value
    public static class Person {
        private String name;
        int age;
        float height;
        float weight;

        public Person(String name, int age, float height, float weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }

    @Value
    public static class Person2 {
        String name;
        int age;
        BigDecimal height;
        BigDecimal weight;

        public Person2(String name, int age, BigDecimal height, BigDecimal weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }

    @Value
    public static class Person3 {
        String name;
        int age;
        int height;
        int weight;

        public Person3(String name, int age, int height, int weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }

    @Value
    public static class Person4 {
        String name;
        int age;
        double height;
        double weight;

        public Person4(String name, int age, double height, double weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }

    @Value
    public static class Person5 {
        String name;
        int age;
        double height;
        BigDecimal weight;
        public Person5(String name, int age, double height, BigDecimal weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }

    @Value
    public static class Person6 {
        String name;
        int age;
        byte height;
        float weight;
        public Person6(String name, int age, byte height, float weight) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
        }
    }
}

