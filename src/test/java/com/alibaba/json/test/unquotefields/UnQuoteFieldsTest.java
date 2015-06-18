/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.alibaba.json.test.unquotefields;

/**
* Created by dean on 15/5/15.
*/

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author shuangxi.dsx
 * @version $$Id: Test, v 0.1 15/5/15 21:18 shuangxi.dsx Exp $$
 */
public class UnQuoteFieldsTest {

    @Test
    public void test() {
        Student student = new Student();
        student.setName("de,an");
        student.setAge(26);
        student.setSex(true);
        student.setAlias("张三");
        List<Student> students = new ArrayList<Student>();
        students.add(student);

        Map<String, String> classRoom = new HashMap<String, String>();
        classRoom.put("aaa", "111");
        School school = new School();
        school.setName("zjg\n,\r\"su");
        school.setStudents(students);
        school.setClassRoom(classRoom);

        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.QuoteFieldNames, false);
            serializer.config(SerializerFeature.UnQuoteFieldValues, true);

            serializer.write(school);

            String json = out.toString();

            School school1 = JSON.parseObject(json, School.class, Feature.AllowUnQuotedFieldValues);

            checkValues(school, school1);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
           
        }

    }

    /**
     * 
     * @param school
     * @param school1
     */
    private void checkValues(School school, School school1) {
        assertEquals(school.getName(), school1.getName());
        List<Student> students = school.getStudents();
        List<Student> students1 = school1.getStudents();
        Student student = students.get(0);
        Student student1 = students1.get(0);
        assertEquals(student.getName(), student1.getName());
        assertEquals(student.getAge(), student1.getAge());
        assertEquals(student.getAlias(), student1.getAlias());
    }
}
