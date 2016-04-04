package com.alibaba.json.test.gson;

import com.google.gson.Gson;

import junit.framework.TestCase;

public class TestChineseQuote extends TestCase {

    public void test_chinese_quote() throws Exception {
        String text = "{\"name\":“tiny.luo”,\"school\":\"\"}";

        Gson gson = new Gson();
        User user = gson.fromJson(text, User.class);
        
    }

    public static class User {

        public String name;
        public String school;
    }

    public static class School {

        public int    id;
        public String address;
    }
}
