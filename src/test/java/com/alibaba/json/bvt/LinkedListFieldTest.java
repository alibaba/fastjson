package com.alibaba.json.bvt;

import java.util.LinkedList;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class LinkedListFieldTest extends TestCase {

    public void test_codec_null() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":null}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_codec_null_1() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        Assert.assertEquals("{value:[]}", JSON.toJSONStringZ(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        Assert.assertEquals("{value:[]}", JSON.toJSONStringZ(v, mapping, SerializerFeature.UseSingleQuotes, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
        Assert.assertEquals("{'value':[]}", JSON.toJSONStringZ(v, mapping, SerializerFeature.UseSingleQuotes, SerializerFeature.QuoteFieldNames, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
    }

    public static class V0 {

        private LinkedList value;

        public LinkedList getValue() {
            return value;
        }

        public void setValue(LinkedList value) {
            this.value = value;
        }

    }
}
