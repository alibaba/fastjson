package com.alibaba.json.bvt.ref;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RefTest11 extends TestCase {

    public void test_ref() throws Exception {
        Department tech = new Department(1, "技术部");
        tech.setRoot(tech);
        
        {
            Department pt = new Department(2, "平台技术部");
            pt.setParent(tech);
            pt.setRoot(tech);
            tech.getChildren().add(pt);
            {
                Department sysbase = new Department(3, "系统基础");
                sysbase.setParent(pt);
                sysbase.setRoot(tech);
                pt.getChildren().add(sysbase);
            }
        }
        {
            Department cn = new Department(4, "中文站技术部");
            cn.setParent(tech);
            cn.setRoot(tech);
            tech.getChildren().add(cn);
        }
        
        {
            //JSON.toJSONString(tech);
        }
        
        {
            String prettyText = JSON.toJSONString(tech, SerializerFeature.PrettyFormat);
            System.out.println(prettyText);
        
            String text = JSON.toJSONString(tech);
            Department dept = JSON.parseObject(text, Department.class);
            Assert.assertTrue(dept == dept.getRoot());
            
            System.out.println(JSON.toJSONString(dept, SerializerFeature.PrettyFormat));
        }
    }

    public static class Department {

        private int                    id;
        private String                 name;

        private Department             parent;
        private Department             root;

        private Collection<Department> children = new ArrayList<Department>();

        public Department(){

        }

        public Department getRoot() {
            return root;
        }

        public void setRoot(Department root) {
            this.root = root;
        }

        public Department(int id, String name){
            this.id = id;
            this.name = name;
        }

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

        public Department getParent() {
            return parent;
        }

        public void setParent(Department parent) {
            this.parent = parent;
        }

        public Collection<Department> getChildren() {
            return children;
        }

        public void setChildren(Collection<Department> children) {
            this.children = children;
        }
        
        public String toString() {
            return "{id:" + id + ",name:" + name + "}";
        }

    }
}
