package com.alibaba.json.test.knowissue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class Bug_for_loveflying extends TestCase {

    public void test_for_loveflying() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setName("loveflying");
        user.setCreateTime(new java.sql.Timestamp(new Date().getTime()));

        UserLog userLog = new UserLog();
        userLog.setId(1l);
        userLog.setUser(user);
        user.getUserLogs().add(userLog);

        userLog = new UserLog();
        userLog.setId(2l);
        userLog.setUser(user);
        user.getUserLogs().add(userLog);

        SerializeConfig mapping = new SerializeConfig();

        mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        mapping.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        // mapping.put(User.class, new JavaBeanSerializer(User.class,
        // Collections.singletonMap("id", "uid")));

        JSONObject jsonObject = (JSONObject) JSON.toJSON(user);
        jsonObject.put("ext", "新加的属性");
        System.out.println(jsonObject.toJSONString(jsonObject, mapping));
    }

    public static class UserLog {

        private Long           id;

        private transient User user;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public static class User {

        private Long               id;

        private String             name;

        private java.sql.Timestamp createTime;

        private Set<UserLog>       userLogs = new HashSet<UserLog>();

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

        public java.sql.Timestamp getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.sql.Timestamp createTime) {
            this.createTime = createTime;
        }

        public Set<UserLog> getUserLogs() {
            return userLogs;
        }

        public void setUserLogs(Set<UserLog> userLogs) {
            this.userLogs = userLogs;
        }

    }
}
