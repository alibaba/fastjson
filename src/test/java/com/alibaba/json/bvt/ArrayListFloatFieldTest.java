package com.alibaba.json.bvt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ArrayListFloatFieldTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setValue(new ArrayList<Float>());
        user.getValue().add(1F);

        String text = JSON.toJSONString(user);
        System.out.println(text);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user.getValue(), user1.getValue());
    }

    public static class User {

        private ArrayList<Float> value;
        
        public User() {
            
        }

        public List<Float> getValue() {
            return value;
        }

        public void setValue(ArrayList<Float> value) {
            this.value = value;
        }

    }
}
