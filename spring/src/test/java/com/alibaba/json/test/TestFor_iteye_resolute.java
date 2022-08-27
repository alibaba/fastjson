package com.alibaba.json.test;

import java.io.Serializable;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.lang.SerializationUtils;

import com.alibaba.fastjson.JSON;

public class TestFor_iteye_resolute extends TestCase {

    private static final int SIZE       = 1000;

    private static final int LOOP_COUNT = 1000 * 10;
    
    public void test_perf() {
        for (int i = 0; i < 10; ++i) {
            json();
            javaSer();
            System.out.println();
        }
    }

    public void json() {
        long begin = System.currentTimeMillis();
        int length = 0;
        for (int i = 0; i < LOOP_COUNT; ++i) {
            String json = JSON.toJSONString(mkTestDates(SIZE));
            length = json.length();
        }
        long time = System.currentTimeMillis() - begin;
        System.out.println("json time " + time + ", len " + length);
        
    }

    public void javaSer() {
        long begin = System.currentTimeMillis();
        int length = 0;
        for (int i = 0; i < LOOP_COUNT; ++i) {
            byte[] bytes = SerializationUtils.serialize(mkTestDates(SIZE));
            length = bytes.length;
        }
        
        long time = System.currentTimeMillis() - begin;
        System.out.println("java time " + time + ", len " + length);
    }

    public ArrayList<User> mkTestDates(int count) {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < count; i++) {
            User user = new User(i);
            user.setName("xxxxxxxxxxxxxxxxxxxxxx");
            users.add(user);
        }
        return users;
    }

    public static class User implements Serializable {

        private static final long serialVersionUID = 1L;
        private int               id;
        private String            name;

        public User(int id){
            super();
            this.id = id;
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

}
