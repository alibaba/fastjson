package com.alibaba.json.bvt.bug;

import java.util.List;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_yuanmomo_Issue_505_1 extends TestCase {

    public void test_for_issue() throws Exception {
        String userStr1 = "{\"id\":\"qfHdV0ez0N10\", \"ext\":{\"model\": \"10000\"} }";
        User user = JSON.parseObject(userStr1, User.class);
        System.out.println(user);
    }

    public void test_for_issue_1() throws Exception {
        String text = "{\"model\":\"10002\" }";
        UserExt ext = JSON.parseObject(text, UserExt.class);
    }

    public void test_for_issue_2() throws Exception {
        String userStr2 = "{\"id\":\"qfHdV0ez0N10\", \"ext\":{\"model\":\"10000\" } }";

        User user = JSON.parseObject(userStr2, User.class);
        System.out.println(user);
    }

    public static class User {

        private String  id;
        private UserExt ext;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public UserExt getExt() {
            return ext;
        }

        public void setExt(UserExt ext) {
            this.ext = ext;
        }

        @Override
        public String toString() {
            return "User{" + "id='" + id + '\'' + ", ext=" + ext + '}';
        }
    }

    public static class UserExt {

        private String model;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String toString() {
            return "UserExt{model=" + model + "}";
        }
    }
}
