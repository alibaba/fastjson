package com.alibaba.json.bvt.issue_3700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3732 extends TestCase {
    static class InnerClass {
        int id;

        public InnerClass() {}

        public InnerClass(int id) {
            this.id = id;
        }

        public void setId(int id) {this.id = id;}

        public int getId() {
            return id;
        }
    }

    static class OuterClass {
        List<InnerClass> innerClassList;

        public void setRoleList(List<InnerClass> innerClassList) {
            this.innerClassList = innerClassList;
        }

        public List<InnerClass> getRoleList() {
            return innerClassList;
        }

        public List<Integer> getRoleIdList(){
            if (innerClassList == null) return null;
            List<Integer> roleIdList = new ArrayList<Integer>();
            for (InnerClass innerClass : innerClassList) {
                roleIdList.add(innerClass.id);
            }
            return roleIdList;
        }

        public int getTest(){
            return 0;
        }

        @Override
        public boolean equals(Object other){
            if (other instanceof OuterClass) {
                List<Integer> otherIdList = ((OuterClass) other).getRoleIdList();
                return this.getRoleIdList().equals(otherIdList);
            }
            return false;
        }
    }

    static class SingleClass{
        int value;

        public SingleClass() {}

        public SingleClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValueValue(){
            return value;
        }
    }

    @Test
    public void test_for_nested_class(){
        InnerClass innerClass1 = new InnerClass(1);
        InnerClass innerClass2 = new InnerClass(2);
        InnerClass innerClass3 = new InnerClass(3);
        List<InnerClass> innerClassList = new ArrayList<InnerClass>();
        innerClassList.add(innerClass1);
        innerClassList.add(innerClass2);
        innerClassList.add(innerClass3);
        OuterClass outerClass = new OuterClass();
        outerClass.innerClassList = innerClassList;

        String jsonString = JSON.toJSONString(outerClass);
        System.out.println(jsonString);

        OuterClass parseOuterClass = JSON.parseObject(jsonString, OuterClass.class);
        assertEquals(outerClass, parseOuterClass);
    }

    @Test
    public void test_for_single_class(){
        SingleClass singleClass = new SingleClass(100);
        String jsonString = JSON.toJSONString(singleClass);
        System.out.println(jsonString);

        SingleClass parseSingleClass = JSON.parseObject(jsonString, SingleClass.class);
        assertEquals(singleClass.value,parseSingleClass.value);
    }
}