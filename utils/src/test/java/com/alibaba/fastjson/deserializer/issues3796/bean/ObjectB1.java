package com.alibaba.fastjson.deserializer.issues3796.bean;

import java.util.List;

public class ObjectB1 {
    
    List<ObjectI_A> a;

    
    private int b;

    public List<ObjectI_A> getA() {
        return a;
    }

    public void setA(List<ObjectI_A> a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
