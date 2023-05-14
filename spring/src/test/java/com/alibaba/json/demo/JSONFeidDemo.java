package com.alibaba.json.demo;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class JSONFeidDemo extends TestCase {

    public static class User {

        private int    id;
        private String name;

        @JSONField(name = "uid")
        public int getId() { return id; }

        @JSONField(name = "uid")
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }

        public void setName(String name) { this.name = name; }
    }

    public void test_0() throws Exception {
        User user = new User();
        user.setId(123);
        user.setName("毛头");

        String text = JSON.toJSONString(user);
        Assert.assertEquals("{\"name\":\"毛头\",\"uid\":123}", text);
        System.out.println(text);
    }
}
