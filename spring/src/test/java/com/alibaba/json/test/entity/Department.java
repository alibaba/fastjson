package com.alibaba.json.test.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class Department {

    private Long             id;
    private String           name;
    private String           description;

    private List<Department> children = new ArrayList<Department>();
    private List<Employee>   members  = new ArrayList<Employee>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Employee> getMembers() {
        return members;
    }

    public void setMembers(List<Employee> members) {
        this.members = members;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void setChildren(List<Department> children) {
        this.children = children;
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
