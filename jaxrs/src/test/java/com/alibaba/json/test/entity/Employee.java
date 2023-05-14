package com.alibaba.json.test.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {

    private Long       id;
    private String     number;
    private String     name;
    private String     description;
    private Integer    age;
    private BigDecimal salary;
    private Date       birthdate;
    private boolean    badboy;

    public Employee(){

    }

    public boolean isBadboy() {
        return badboy;
    }

    public void setBadboy(boolean badboy) {
        this.badboy = badboy;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

}
