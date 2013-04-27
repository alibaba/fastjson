package com.alibaba.json.test.entity;

public class Company {

    private Long       id;
    private String     name;
    private String     description;
    private String     stock;

    private Department rootDepartment;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Department getRootDepartment() {
        return rootDepartment;
    }

    public void setRootDepartment(Department rootDepartment) {
        this.rootDepartment = rootDepartment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
