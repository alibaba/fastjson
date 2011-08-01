package com.alibaba.json.demo;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class EncodeDemo extends TestCase {

    public void test_encode() throws Exception {
        Group group = new Group();
        group.setId(0L);
        group.setName("admin");

        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");

        User rootUser = new User();
        rootUser.setId(3L);
        rootUser.setName("root");

        group.getUsers().add(guestUser);
        group.getUsers().add(rootUser);

        String jsonString = JSON.toJSONString(group);

        System.out.println(jsonString);
        
        JSON.parseObject(jsonString, Group.class);
    }

}
