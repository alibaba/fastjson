package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue1723 extends TestCase {
    public void test_for_issue() throws Exception {
        User user = JSON.parseObject("{\"age\":\"0.9390308260917664\"}", User.class);
        assertEquals(0.9390308F, user.age);
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
