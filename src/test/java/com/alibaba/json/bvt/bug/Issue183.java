package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Issue183 extends TestCase {

    public void test_issue_183() throws Exception {
        A a = new A();
        a.setName("xiao").setAge(21);
        String result = JSON.toJSONString(a);
        A newA = JSON.parseObject(result, A.class);
        Assert.assertTrue(a.equals(newA));
    }

    static interface IA {

        @JSONField(name = "wener")
        String getName();
        
         @JSONField(name = "wener")
         IA setName(String name);
    }

    static class A implements IA {

        String name;
        int    age;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public A setAge(int age) {
            this.age = age;
            return this;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            A other = (A) obj;
            if (age != other.age) return false;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            return true;
        }
        
    }
}
