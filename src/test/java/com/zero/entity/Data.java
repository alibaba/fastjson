package com.zero.entity;

import com.alibaba.fastjson.annotation.JSONString;

import java.util.List;

public class Data {
    private String name;
    private String value;
    @JSONString
    private Person child;
    @JSONString
    private List<Person> list;
    public Data(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public Data() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Person getChild() {
        return child;
    }

    public void setChild(Person child) {
        this.child = child;
    }

    public List<Person> getList() {
        return list;
    }

    public void setList(List<Person> list) {
        this.list = list;
    }
}
