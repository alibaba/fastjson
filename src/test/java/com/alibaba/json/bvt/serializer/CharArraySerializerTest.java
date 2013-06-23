package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import junit.framework.TestCase;

public class CharArraySerializerTest extends TestCase {

    public void test_null() throws Exception {
        VO vo = new VO();
        
        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }

    private static class VO {

        private char[] value;

        public char[] getValue() {
            return value;
        }

        public void setValue(char[] value) {
            this.value = value;
        }

    }
}
