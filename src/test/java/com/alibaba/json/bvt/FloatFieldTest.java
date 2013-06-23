package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class FloatFieldTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setValue(1001F);

        String text = JSON.toJSONString(user);
        System.out.println(text);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertTrue(user1.getValue() == user.getValue());
    }

    public static class User {

        private float value;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

    }
}
