package com.alibaba.json.demo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Demo2 extends TestCase {

    public void test_0() throws Exception {
        Department dep = new Department();
        dep.setId(123);
        dep.setName("一级部门");
        dep.setParent(dep);        

        String text = JSON.toJSONString(dep);
        System.out.println(text);
        
        JSON.parseObject(text, Department.class);
    }

    public static class Department {
    
        private int                  id;
        private String               name;
        private Department parent;
        private transient List<Department> children = new ArrayList<Department>();
    
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        @JSONField(serialize=false)
        public Department getParent() { return parent; }
        public void setParent(Department parent) { this.parent = parent; }
        public List<Department> getChildren() { return children; }
        public void setChildren(List<Department> children) { this.children = children; }
    }
}
