package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * @Author ：Nanqi
 * @Date ：Created in 17:58 2020/6/26
 */
public class Issue3277 extends TestCase {
    public void test_for_issue() {
        includes();
        ignores();
    }

    private void includes() {
        String jsonString = "{\"id\":\"1001\",\"name\":\"Test1\",\"password\":\"a@b@c\"}";
        UserEntityUseIncludes user = JSON.parseObject(jsonString, UserEntityUseIncludes.class);
        Assert.assertEquals(user.getPassword(), null, null);
        Assert.assertEquals(user.getId(), 0, 0);
    }

    private void ignores() {
        String jsonString = "{\"id\":\"1001\",\"name\":\"Test1\",\"password\":\"a@b@c\"}";
        UserEntityUseIgnores user = JSON.parseObject(jsonString, UserEntityUseIgnores.class);
        Assert.assertEquals(user.getPassword(), null, null);
    }

    @JSONType(includes = { "name" })
    protected static class UserEntityUseIncludes {

        private String id;
        private String password;
        private String name;

        public UserEntityUseIncludes(String id, String password) {
            this.id = id;
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JSONType(ignores = { "password"})
    protected static class UserEntityUseIgnores {

        private String password;
        private int id;
        private String name;

        public UserEntityUseIgnores(String password, int id, String name) {
            this.password = password;
            this.id = id;
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
