package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Issue183 extends TestCase {

    public void test_issue_183() throws Exception {

    }

    static interface IA {

        @JSONField(name = "wener")
        String getName();
        // @JSONField(name = "wener")
        // IA setName(String name);
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

    }

    @Test
    public void test() {
        A a = new A();
        a.setName("xiao").setAge(21);
        String result = JSON.toJSONString(a);
        A newA = JSON.parseObject(result, A.class);
        assert a.equals(newA);
    }
}
