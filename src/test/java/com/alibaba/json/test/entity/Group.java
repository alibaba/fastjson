package com.alibaba.json.test.entity;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String        name;
    private String        description;

    private List<Company> companies = new ArrayList<Company>();

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
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
