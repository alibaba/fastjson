package com.alibaba.fastjson.deserializer.issue3259;

public class Test2Vo {
    private Test3Vo a;

    private Test4Vo b;


    public Test3Vo getA() {
        return a;
    }

    public void setA(Test3Vo a) {
        this.a = a;
    }

    public Test4Vo getB() {
        return b;
    }

    public void setB(Test4Vo b) {
        this.b = b;
    }
}