package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class AnnotationTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setId(1001);
        user.setName("bob.panl");
        user.setDescrition("大黄牛");

        String text = JSON.toJSONString(user);
        System.out.println(text);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getId(), user.getId());
        Assert.assertEquals(user1.getName(), user.getName());
    }

    public static class User {

        private int    id;
        private String name;
        private String descrition;

        @JSONField(name = "ID")
        public int getId() {
            return id;
        }

        @JSONField(name = "ID")
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JSONField(name = "desc")
        public String getDescrition() {
            return descrition;
        }

        @JSONField(name = "desc")
        public void setDescrition(String descrition) {
            this.descrition = descrition;
        }

    }
}
