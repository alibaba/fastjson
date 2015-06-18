package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.serializer.BooleanFieldSerializerTest.Entity;

public class BooleanFieldSerializerTest_primitive extends TestCase {

    public void test_0() {
        Assert.assertEquals("{\"value\":false}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse));
    }

    public void test_codec_no_asm() throws Exception {
        Entity v = new Entity();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":false}", text);

        Entity v1 = JSON.parseObject(text, Entity.class);

        Assert.assertEquals(v.getValue(), v1.getValue());
    }

    public void test_codec() throws Exception {
        Entity v1 = parseObjectNoAsm("{value:1}", Entity.class, JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertEquals(true, v1.getValue());
    }

    public void test_codec_0() throws Exception {
        Entity v1 = parseObjectNoAsm("{value:0}", Entity.class, JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertEquals(false, v1.getValue());
    }

    public void test_codec_1() throws Exception {
        Entity v1 = parseObjectNoAsm("{value:'true'}", Entity.class, JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertEquals(true, v1.getValue());
    }

    public void test_codec_2() throws Exception {
        Entity v1 = parseObjectNoAsm("{value:null}", Entity.class, JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertEquals(false, v1.getValue());
    }
    
    public void test_codec_3() throws Exception {
        Entity v1 = parseObjectNoAsm("{value:\"\"}", Entity.class, JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertEquals(false, v1.getValue());
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObjectNoAsm(String input, Type clazz, int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        ParserConfig config = new ParserConfig();
        config.setAsmEnable(false);

        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, config, featureValues);
        T value = (T) parser.parseObject(clazz);

        if (clazz != JSONArray.class) {
            parser.close();
        }

        return (T) value;
    }

    public static class Entity {

        private boolean value;

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

    }
}
