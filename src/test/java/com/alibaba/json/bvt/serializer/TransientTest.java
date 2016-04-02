package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import junit.framework.TestCase;

public class TransientTest extends TestCase {

    public void test_transient() throws Exception {
        Category parent = new Category();
        parent.setName("Parent");

        Category child = new Category();
        child.setName("child");

        parent.addChild(child);

        String text = JSON.toJSONString(parent);
        System.out.println(text);

        Assert.assertNotNull(TypeUtils.getField(Category.class, "name", Category.class.getDeclaredFields()));
        Assert.assertNull(TypeUtils.getField(Category.class, "abc", Category.class.getDeclaredFields()));
    }

    public static class Category {

        private String             name;
        private transient Category parent;

        private List<Category>     children = new ArrayList<Category>();

        public void addChild(Category child) {
            children.add(child);
            child.setParent(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category getParent() {
            return parent;
        }

        public void setParent(Category parent) {
            this.parent = parent;
        }

        public List<Category> getChildren() {
            return children;
        }

        public void setChildren(List<Category> children) {
            this.children = children;
        }

    }
}
