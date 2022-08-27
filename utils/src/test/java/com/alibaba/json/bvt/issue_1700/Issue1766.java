package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.Date;

public class Issue1766 extends TestCase {
    public void test_for_issue() throws Exception {
// succ
        String json = "{\"name\":\"张三\"\n, \"birthday\":\"2017-01-01 01:01:01\"}";
        User user = JSON.parseObject(json, User.class);
        assertEquals("张三", user.getName());
        assertNotNull(user.getBirthday());

        // failed
        json = "{\"name\":\"张三\", \"birthday\":\"2017-01-01 01:01:02\"\n}";
        user = JSON.parseObject(json, User.class);// will exception
        assertEquals("张三", user.getName());
        assertNotNull(user.getBirthday());
    }

    public static class User
    {
        private String name;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Date birthday;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Date getBirthday()
        {
            return birthday;
        }

        public void setBirthday(Date birthday)
        {
            this.birthday = birthday;
        }
    }
}
