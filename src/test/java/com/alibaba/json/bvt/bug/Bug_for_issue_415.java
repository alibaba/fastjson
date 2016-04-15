package com.alibaba.json.bvt.bug;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_415 extends TestCase {

    public void test_for_issue() throws Exception {
        Teacher t = new Teacher();

        Address addr = new Address();
        addr.setAddrDetail("中国上海南京路");

        Student s1 = new Student();
        s1.setName("张三");
        s1.setAddr(addr);

        Student s2 = new Student();
        s2.setName("李四");
        s2.setAddr(addr);

        t.setStudentList(Arrays.asList(s1, s2));
        
        String json = JSON.toJSONString(t,SerializerFeature.WriteClassName);
        //@1 打印序列化的时候json串

        Teacher t2 = (Teacher) JSON.parse(json);
        for (Student s : t2.getStudentList()) {
            Assert.assertNotNull(s);
            Assert.assertNotNull(s.getAddr());
        }
    }

    public static class Teacher {

        private List<Student> studentList;

        public List<Student> getStudentList() {
            return studentList;
        }

        public void setStudentList(List<Student> studentList) {
            this.studentList = studentList;
        }

    }

    public static class Address {

        private String addrDetail;

        public String getAddrDetail() {
            return addrDetail;
        }

        public void setAddrDetail(String addressDetail) {
            this.addrDetail = addressDetail;
        }
    }

    public static class Student {

        private String  name;
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Address getAddr() {
            return address;
        }

        public void setAddr(Address address) {
            this.address = address;
        }

    }
}
