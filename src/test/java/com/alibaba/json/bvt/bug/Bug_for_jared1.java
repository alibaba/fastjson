package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_jared1 extends TestCase {
    public void test_for_jared1() throws Exception {
        User user = new User();

        String text = JSON.toJSONString(user);
        
        System.out.println(text);
        
        JSON.parseObject(text, User.class);
    }

    public static class User implements Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        private Integer           id;

        private String            acount;

        private String            password;

        private Set<Crowd>        crowds           = new HashSet<Crowd>();

        private Set<User>         friends          = new HashSet<User>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAcount() {
            return acount;
        }

        public void setAcount(String acount) {
            this.acount = acount;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Set<Crowd> getCrowds() {
            return crowds;
        }

        public void setCrowds(Set<Crowd> crowds) {
            this.crowds = crowds;
        }

        public Set<User> getFriends() {
            return friends;
        }

        public void setFriends(Set<User> friends) {
            this.friends = friends;
        }

        // 一下省略

    }

    public static class Crowd {

    }
}
