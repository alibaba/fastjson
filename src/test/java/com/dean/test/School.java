/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.dean.test;/**
 * Created by dean on 15/5/15.
 */

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author shuangxi.dsx
 * @version $$Id: School, v 0.1 15/5/15 21:14 shuangxi.dsx Exp $$
 */
public class School {

    @JSONField(name = "sn")
    private String name;

    @JSONField(name = "ss")
    private List<Student> students;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
