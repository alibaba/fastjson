package com.alibaba.json.bvt.asm;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class LoopTest extends TestCase {
    
    public void test_loop() throws Exception {
        Department department = JSON.parseObject("{id:12,name:'R & D', members:[{id:2001, name:'jobs'}]}", Department.class);
        Assert.assertNotNull(department);
        Assert.assertEquals(12, department.getId());
    }

    public static class Department {

        private int            id;
        private String         name;

        private List<Employee> members = new ArrayList<Employee>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Employee> getMembers() {
            return members;
        }

        public void setMembers(List<Employee> members) {
            this.members = members;
        }

    }

    public static class Employee {

        private int        id;
        private String     name;

        private Department department;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

    }
}
