package com.alibaba.json.bvt;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.test.TestUtils;

public class ByteArrayFieldTest_3 extends TestCase {

    public void test_0() throws Exception {
        Entity entity = new Entity("中华人民共和国");
        String text = JSON.toJSONString(entity);
        JSONObject json = JSON.parseObject(text);
        Assert.assertEquals(TestUtils.encodeToBase64String(entity.getValue(), false), json.getString("value"));
        
        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals("中华人民共和国", new String(entity2.getValue(), "UTF-8"));
    }

    private static class Entity {

        private byte[] value;

        public Entity(){

        }

        public Entity(String value) throws UnsupportedEncodingException{
            this.value = value.getBytes("UTF-8");
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

    }
}
