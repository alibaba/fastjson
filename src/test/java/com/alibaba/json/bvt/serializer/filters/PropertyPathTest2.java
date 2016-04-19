package com.alibaba.json.bvt.serializer.filters;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

public class PropertyPathTest2 extends TestCase {

    public void test_path() throws Exception {
        Person p1 = new Person();
        p1.setId(100);
        
        Person c1 = new Person();
        c1.setId(1000);
        
        Person c2 = new Person();
        c2.setId(2000);
        
        p1.getChildren().add(c1);
        p1.getChildren().add(c2);
        
        Assert.assertEquals("{\"children\":[{\"id\":1000},{\"id\":2000}],\"id\":100}", JSON.toJSONString(p1, new MyPropertyPreFilter()));
    }

    public static class MyPropertyPreFilter implements PropertyPreFilter {

        public boolean apply(JSONSerializer serializer, Object source, String name) {
            String path = serializer.getContext().toString() + "." + name;

            if (path.endsWith("].children")) {
                return false;
            }

            return true;
        }

    }

    public static class Person {

        private int          id;

        private List<Person> children = new ArrayList<Person>();

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Person> getChildren() {
            return children;
        }

        public void setChildren(List<Person> children) {
            this.children = children;
        }

    }
}
