package com.alibaba.fastjson.deserializer.issues3796.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ObjectB2 {


    
    @JSONField(serialize = false)
    private int a = 1;


    
    @JSONField(serialize = false)
    private long b;


    private List<Boolean> c;


    private List<Integer> d;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }

    public List<Boolean> getC() {
        return c;
    }

    public void setC(List<Boolean> c) {
        this.c = c;
    }

    public List<Integer> getD() {
        return d;
    }

    public void setD(List<Integer> d) {
        this.d = d;
    }
}
