package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectP1 {
    
    private int a;
    
    private int b;
    
    private int c;
    
    private List<Long> d;
    
    private int e = 0;
    
    private int f = 0;
    
    private List<ObjectP1_A> g;
    
    private List<Integer> h;
    
    private List<ObjectP1_B> i;
    
    private boolean j = true;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public List<Long> getD() {
        return d;
    }

    public void setD(List<Long> d) {
        this.d = d;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public List<ObjectP1_A> getG() {
        return g;
    }

    public void setG(List<ObjectP1_A> g) {
        this.g = g;
    }

    public List<Integer> getH() {
        return h;
    }

    public void setH(List<Integer> h) {
        this.h = h;
    }

    public List<ObjectP1_B> getI() {
        return i;
    }

    public void setI(List<ObjectP1_B> i) {
        this.i = i;
    }

    public boolean isJ() {
        return j;
    }

    public void setJ(boolean j) {
        this.j = j;
    }
}
