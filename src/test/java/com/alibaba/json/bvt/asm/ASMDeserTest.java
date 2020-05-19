package com.alibaba.json.bvt.asm;

import java.util.ArrayList;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ASMDeserTest extends TestCase {

    public void test_codec() throws Exception {
        String text = JSON.toJSONString(new Entity());

        Assert.assertEquals("[]", text);

        Entity object = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(0, object.size());
    }
    
    public void test_codec_1() throws Exception {
        String text = JSON.toJSONString(new VO());

        Assert.assertEquals("{\"value\":[]}", text);

        VO object = JSON.parseObject(text, VO.class);
        Assert.assertEquals(0, object.getValue().size());
    }

    public void test_ArrayList() throws Exception {

        ArrayList object = JSON.parseObject("[]", ArrayList.class);
        Assert.assertEquals(0, object.size());
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[]", EntityError.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private Entity value = new Entity();

        public Entity getValue() {
            return value;
        }

        public void setValue(Entity value) {
            this.value = value;
        }

    }

    public static class Entity extends ArrayList<String> {

    }

    public static class EntityError extends ArrayList<String> {

        public EntityError(){
            throw new RuntimeException();
        }
    }
}
