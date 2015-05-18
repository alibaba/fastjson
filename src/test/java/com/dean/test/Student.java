/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.dean.test;/**
 * Created by dean on 15/5/15.
 */

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author shuangxi.dsx
 * @version $$Id: Student, v 0.1 15/5/15 21:13 shuangxi.dsx Exp $$
 */
public class Student {

    @JSONField(name = "n")
    private String name;

    @JSONField(name = "a")
    private long age;

    @JSONField(name = "s")
    private boolean sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
