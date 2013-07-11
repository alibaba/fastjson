package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMClassLoader;

public class ClassFieldTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setValue(Object.class);

        String text = JSON.toJSONString(user);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getValue(), user.getValue());
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"value\":123}", User.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class User {

        private Class<?> value;

        public Class<?> getValue() {
            return value;
        }

        public void setValue(Class<?> value) {
            this.value = value;
        }

    }
}
