package com.alibaba.fastjson.deserializer.issues3796.bean;




import java.util.List;


public class ObjectW1 {

    
    private int a;

    
    private int b;

    
    private List<Boolean> c;

    
    private boolean d;

    
    private List<CommonObject> e;

    
    private int f = 0;

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

    public List<Boolean> getC() {
        return c;
    }

    public void setC(List<Boolean> c) {
        this.c = c;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public List<CommonObject> getE() {
        return e;
    }

    public void setE(List<CommonObject> e) {
        this.e = e;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }
}
