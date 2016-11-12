package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 2016/11/13.
 */
public class Issue688 extends TestCase {
    public void test_for_issue() throws Exception {
        User monther = new User("HanMeiMei", 29L);
        User child = new User("liLei", 2L);
        User grandma = new User("zhangHua", 60L);

        Map<User, User> userMap = new HashMap<User, User>();
        userMap.put(child, monther);
        userMap.put(monther, grandma);

        String json = JSON.toJSONString(userMap);
        System.out.println(json);
    }

    public static class User {
        public String name;
        public long id;

        public User() {

        }

        public User(String name, long id) {
            this.name = name;
            this.id = id;
        }
    }
}
