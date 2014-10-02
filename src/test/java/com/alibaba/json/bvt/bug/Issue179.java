package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Issue179 extends TestCase {

    public void test_for_issue_179() throws Exception {
        Student student = new Student();
        School school = new School();
        school.setStudent(student);
        student.setSchool(school);

        // String schoolJSONString = JSON.toJSONString(school);
        // System.out.println(schoolJSONString);
        //
        // School fromJSONSchool = JSON.parseObject(schoolJSONString,
        // School.class);
        //
        // System.out.println(JSON.toJSONString(fromJSONSchool));

        JSONObject object = new JSONObject();
        object.put("school", school);

        String jsonString = JSON.toJSONString(object);
        System.out.println(jsonString);

        JSONObject object2 = (JSONObject) JSON.parseObject(jsonString, JSONObject.class);
        System.out.println(JSON.toJSONString(object2));

        School school2 = object2.getObject("school", School.class);
        System.out.println(school2);
    }

    public static class School {

        Student student;

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }
    }

    static class Student {

        public School getSchool() {
            return school;
        }

        public void setSchool(School school) {
            this.school = school;
        }

        School school;
    }
}
