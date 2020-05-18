package com.alibaba.fastjson.deserializer.issue3050.beans;

/**
 * issue3050
 *
 * @author yangy
 * @since 2020年05月03日
 */
public class Person {
    private String name;
    private String address;
    private String id;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", age=" + age +
                '}';
    }
}
