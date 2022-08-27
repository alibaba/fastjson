package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.HashMap;
import java.util.List;


public class ObjectL1 {
    
    
    List<ObjectL1_A> a;
    
    int b;
    
    int c;
    
    int d;

    
    long e;

    long f;

    List<ObjectL2_B> g;
    
    List<CommonObject> h;

    HashMap<Integer, HashMap<Integer, ObjectL2_C>> i;
    
    boolean j = false;

    public List<ObjectL1_A> getA() {
        return a;
    }

    public void setA(List<ObjectL1_A> a) {
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

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    public long getF() {
        return f;
    }

    public void setF(long f) {
        this.f = f;
    }

    public List<ObjectL2_B> getG() {
        return g;
    }

    public void setG(List<ObjectL2_B> g) {
        this.g = g;
    }

    public List<CommonObject> getH() {
        return h;
    }

    public void setH(List<CommonObject> h) {
        this.h = h;
    }

    public HashMap<Integer, HashMap<Integer, ObjectL2_C>> getI() {
        return i;
    }

    public void setI(HashMap<Integer, HashMap<Integer, ObjectL2_C>> i) {
        this.i = i;
    }

    public boolean isJ() {
        return j;
    }

    public void setJ(boolean j) {
        this.j = j;
    }
}
