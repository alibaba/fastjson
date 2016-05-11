package com.alibaba.json.demo;

import com.alibaba.fastjson.JSON;

/**
 * Created by hanzhankang on 15/9/30.
 */
public class TestCase {
    boolean isPromiseSale1;
    Boolean isPromiseSale2;
    String isName;
    String getName;
    String name;

    public boolean isPromiseSale1() {
        return isPromiseSale1;
    }

    public void setIsPromiseSale1(boolean isPromiseSale1) {
        this.isPromiseSale1 = isPromiseSale1;
    }

    public Boolean isPromiseSale2() {
        return isPromiseSale2;
    }

    public void setIsPromiseSale2(Boolean isPromiseSale2) {
        this.isPromiseSale2 = isPromiseSale2;
    }

    public String getIsName() {
        return isName;
    }

    public void setIsName(String isName) {
        this.isName = isName;
    }

    public String getGetName() {
        return getName;
    }

    public void setGetName(String getName) {
        this.getName = getName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        TestCase tb = new TestCase();
        tb.setIsPromiseSale1(true);
        tb.setIsPromiseSale2(true);
        tb.setName("name");
        tb.setGetName("getName");
        tb.setIsName("isName");
        System.out.println("结果输出："+JSON.toJSON(tb));
    }
}
