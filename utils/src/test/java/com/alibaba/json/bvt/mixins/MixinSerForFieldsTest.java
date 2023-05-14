package com.alibaba.json.bvt.mixins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class MixinSerForFieldsTest extends TestCase {
    static class BeanClass {
        public String a;
        public String b;

        public BeanClass(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    abstract class MixIn {
        @JSONField(serialize = false)
        public String a;
        @JSONField(name = "banana")
        public String b;
    }

    public void test() throws Exception{
        BeanClass bean = new BeanClass("1", "2");

        JSON.addMixInAnnotations(BeanClass.class, MixIn.class);
        String jsonString = JSON.toJSONString(bean);
        JSONObject result = JSON.parseObject(jsonString);
        assertEquals(1, result.size());
        assertEquals("2", result.get("banana"));
        JSON.removeMixInAnnotations(BeanClass.class);
    }
}
