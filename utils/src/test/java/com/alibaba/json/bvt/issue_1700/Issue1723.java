package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue1723 extends TestCase {
    public void test_for_issue() throws Exception {
        User user = JSON.parseObject("{\"age\":\"0.9390308260917664\"}", User.class);
        assertEquals(0.9390308260917664F, user.age);
    }

    public void test_for_issue_1() throws Exception {
        User user = JSON.parseObject("{\"age\":\"8.200000000000001\"}", User.class);
        assertEquals(8.200000000000001F, user.age);
    }

    public void test_for_issue_2() throws Exception {
        User user = JSON.parseObject("[\"8.200000000000001\"]", User.class, Feature.SupportArrayToBean);
        assertEquals(8.200000000000001F, user.age);
    }

    public static class User {
        private float age;
        public float getAge() {
            return age;
        }
        public void setAge(float age) {
            this.age = age;
        }
        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    '}';
        }
    }
}
