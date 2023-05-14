package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue3470 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = JSON.toJSONString(new Privacy().setPassword("test"));
        assertEquals("{\"__password\":\"test\"}", str);
    }

    public static class Privacy {
        private String phone; //手机
        private String password; //登录密码，隐藏字段

        public Privacy() {
            super();
        }

        public String getPhone() {
            return phone;
        }
        public Privacy setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public String get__password() {
            return password;
        }
        public Privacy setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
