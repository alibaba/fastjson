package com.alibaba.json.test.entity.case2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Category implements Serializable {

    private static final long serialVersionUID = 8606386680333098397L;
    private long              id;
    private String            name;

    private Category          parent;
    private List<Category>    children         = new ArrayList<Category>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(serialize = false)
    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

}
