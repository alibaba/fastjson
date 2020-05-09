package com.alibaba.json.bvt;

import java.util.UUID;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class UUIDFieldTest extends TestCase {

    public void test_codec() throws Exception {
        User user = new User();
        user.setValue(UUID.randomUUID());

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        String text = JSON.toJSONString(user, mapping, SerializerFeature.WriteMapNullValue);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getValue(), user.getValue());
    }
    public void test_codec_upper_case() throws Exception {
       User  user = new User();

       String text ="{\"value\":\"79104776-6CA7-4E41-948F-4D2ECD06502A\"}";
       user = JSON.parseObject(text,  User.class);

       Assert.assertEquals("79104776-6CA7-4E41-948F-4D2ECD06502A", user.getValue().toString().toUpperCase());	   
    }  

    public void test_codec_null() throws Exception {
        User user = new User();
        user.setValue(null);

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        String text = JSON.toJSONString(user, mapping, SerializerFeature.WriteMapNullValue);

        User user1 = JSON.parseObject(text, User.class);

        Assert.assertEquals(user1.getValue(), user.getValue());
    }

    public static class User {

        private UUID value;

        public UUID getValue() {
            return value;
        }

        public void setValue(UUID value) {
            this.value = value;
        }

    }
}
