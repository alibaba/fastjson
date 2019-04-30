package com.alibaba.json.bvt.bug.bug2397;

import java.io.Serializable;

public class Msg implements Serializable{
    private int id;
    private String name;

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

    public Msg(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Msg(){

    }
}
