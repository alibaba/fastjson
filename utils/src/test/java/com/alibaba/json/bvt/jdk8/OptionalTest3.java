package com.alibaba.json.bvt.jdk8;

import java.util.Optional;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OptionalTest3 extends TestCase {

    public void test_optional() throws Exception {
        UserExt ext = new UserExt();
        ext.setValue(Optional.of(123));
        User user = new User();
        user.setExt(Optional.of(ext));

        String text = JSON.toJSONString(user);

        Assert.assertEquals("{\"ext\":{\"value\":123}}", text);

        User user2 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user.getExt().get().getValue().get(), user2.getExt().get().getValue().get());
    }

    public static class User {

        private Optional<UserExt> ext;

        public Optional<UserExt> getExt() {
            return ext;
        }

        public void setExt(Optional<UserExt> ext) {
            this.ext = ext;
        }

    }

    public static class UserExt {

        private Optional<Integer> value;

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
