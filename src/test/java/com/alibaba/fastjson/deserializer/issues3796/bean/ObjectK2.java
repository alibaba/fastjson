package com.alibaba.fastjson.deserializer.issues3796.bean;



import java.util.List;


public class ObjectK2 {
    private int a;
    private List<ObjectK2_A> b;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public List<ObjectK2_A> getB() {
        return b;
    }

    public void setB(List<ObjectK2_A> b) {
        this.b = b;
    }
}
