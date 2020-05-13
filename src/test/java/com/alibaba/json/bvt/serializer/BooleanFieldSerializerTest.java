package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BooleanFieldSerializerTest extends TestCase {

    public void test_0() {
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue));
        Assert.assertEquals("{\"value\":false}", JSON.toJSONString(new Entity(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse));
    }

    public void test_codec_no_asm() throws Exception {
        Entity v = new Entity();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":null}", text);

        Entity v1 = parseObjectNoAsm(text, Entity.class, JSON.DEFAULT_PARSER_FEATURE);

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

    @SuppressWarnings("unchecked")
    public static final <T> T parseObjectNoAsm(String input, Type clazz, int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        ParserConfig config = new ParserConfig();
        config.setAsmEnable(false);

        DefaultJSONParser parser = new DefaultJSONParser(input, config, featureValues);
        T value = (T) parser.parseObject(clazz);

        if (clazz != JSONArray.class) {
            parser.close();
        }

        return (T) value;
    }

    public static class Entity {

        private Boolean value;

        public Boolean getValue() {
            return value;
        }

        public void setValue(Boolean value) {
            this.value = value;
        }

    }
}
