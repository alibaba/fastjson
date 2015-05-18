/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.dean.test;

/**
* Created by dean on 15/5/15.
*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuangxi.dsx
 * @version $$Id: Test, v 0.1 15/5/15 21:18 shuangxi.dsx Exp $$
 */
public class Test {
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("dean");
        student.setAge(26);
        student.setSex(true);
        List<Student> students = new ArrayList<Student>();
        students.add(student);
        School school = new School();
        school.setName("zjgsu");
        school.setStudents(students);

        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.QuoteFieldNames, false);

            serializer.write(school);

            String json = out.toString();
            System.out.println(json);

            School school1 = JSON.parseObject(json, School.class);

            System.out.println(school1);

        } finally {
            out.close();
        }

    }
}
