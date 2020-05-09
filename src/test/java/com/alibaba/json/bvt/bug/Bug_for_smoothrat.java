package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_smoothrat extends TestCase {

    public void test_0() throws Exception {
        Entity entity = new Entity();
        
        entity.setValue("aaa123".toCharArray());
        
        String text = JSON.toJSONString(entity);
        Assert.assertEquals("{\"value\":\"aaa123\"}", text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        
        Assert.assertEquals(new String(entity.getValue()), new String(entity2.getValue()));
    }

    public static class Entity {

        private char[] value;

        public char[] getValue() {
            return value;
        }

        public void setValue(char[] value) {
            this.value = value;
        }

    }
}
