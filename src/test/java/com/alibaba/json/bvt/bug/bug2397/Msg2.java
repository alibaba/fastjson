package com.alibaba.json.bvt.bug.bug2397;

public class Msg2 {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public Msg2(int id, String name) {
        this.id = id;
        this.name = name;
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
}
