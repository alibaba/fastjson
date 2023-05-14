package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;

public class Issue2229 extends TestCase {
    public void test_for_issue() throws Exception {
        Jon jon = JSON.parseObject("{\"dStr\":\"         hahahaha        \",\"user\":{\"createtime\":null,\"id\":0,\"username\":\"  asdfsadf  asdf  asdf  \"}}", Jon.class);
        assertEquals("  asdfsadf  asdf  asdf  ", jon.user.username);
    }

    public void test_for_issue1() throws Exception {
        Jon jon1 = JSON.parseObject("{'dStr':'         hahahaha        ','user':{'createtime':null,'id':0,'username':'  asdfsadf  asdf  asdf  '}}", Jon.class);
        assertEquals("  asdfsadf  asdf  asdf  ", jon1.user.username);
    }

    public static class Jon {
        public String dStr;
        public User user;
    }

    public static class User {
        public int id;
        public Date createtime;
        public String username;
    }
}
