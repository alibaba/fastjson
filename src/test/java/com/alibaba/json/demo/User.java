package com.alibaba.json.demo;


import org.junit.Assert;

import com.alibaba.fastjson.JSON;

public class User {

    private Long   id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static void main(String[] args) {
        User user = new User();
        user.setId(123L);
        user.setName("wenshao");
        String text = JSON.toJSONString(user);
        System.out.println(text);
        
        User user1 = JSON.parseObject(text, User.class);
        Assert.assertEquals("{\"id\":123,\"name\":\"wenshao\"}", text);
    }
}
