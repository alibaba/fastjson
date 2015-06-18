package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_wtusmchen extends TestCase {

    public void test_0() throws Exception {
        List<User> users = new ArrayList<User>();
        users.add(new User());
        users.add(new User());

        String text = JSON.toJSONString(users);
        System.out.println(text);
        
        List<User> users2 = JSON.parseArray(text, User.class);
    }

    public static class User implements Serializable {

        private String user_id = "aaaa";
        Date           bri;
        Timestamp      bri2;
        Double         num;
        List           list;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public Date getBri() {
            return bri;
        }

        public void setBri(Date bri) {
            this.bri = bri;
        }

        public Timestamp getBri2() {
            return bri2;
        }

        public void setBri2(Timestamp bri2) {
            this.bri2 = bri2;
        }

        public Double getNum() {
            return num;
        }

        public void setNum(Double num) {
            this.num = num;
        }

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }

    }
}
