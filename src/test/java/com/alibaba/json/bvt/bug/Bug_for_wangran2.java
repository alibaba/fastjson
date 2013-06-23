package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_wangran2 extends TestCase {

    public void test_for_wangran() throws Exception {
        String text = "{" + //
                      "\"first\":{\"id\":1001}," + //
                      "\"second\":{\"id\":1002,\"root\":{\"$ref\":\"$\"}}," + //
                      "\"id\":23," + //
                      "\"name\":\"xxx\"," + //
                      "\"children\":[{\"root\":{\"$ref\":\"$\"}},{\"$ref\":\"$.second\"}]" + //
                      "}";
        Root root = JSON.parseObject(text, Root.class);
        Assert.assertEquals(23, root.getId());
        Assert.assertEquals("xxx", root.getName());
        Assert.assertTrue(root == root.getChildren().get(0).getRoot());
        Assert.assertTrue(root == root.getChildren().get(1).getRoot());
    }

    public static class Root {

        private int         id;
        private String      name;

        private Child       first;
        private Child       second;

        private List<Child> children = new ArrayList<Child>();

        public Root(){

        }

        public Child getSecond() {
            return second;
        }

        public void setSecond(Child second) {
            System.out.println("setSecond");
            this.second = second;
        }

        public Child getFirst() {
            return first;
        }

        public void setFirst(Child first) {
            System.out.println("setFirst");
            this.first = first;
        }

        public List<Child> getChildren() {
            return children;
        }

        public void setChildren(List<Child> children) {
            System.out.println("setChildren");
            this.children = children;
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

    }

    public static class Child {

        private int  id;

        private Root root;

        public Child(){

        }

        public Root getRoot() {
            return root;
        }

        public void setRoot(Root root) {
            System.out.println("setRoot");
            this.root = root;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
// 500m / 300
