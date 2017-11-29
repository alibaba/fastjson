package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.annotation.JSONType;

import java.util.Date;

@JSONType
public class User {

    private Long id;
    private String name;
    private Date createdOn;

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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

}