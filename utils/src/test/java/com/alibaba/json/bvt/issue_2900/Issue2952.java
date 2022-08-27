package com.alibaba.json.bvt.issue_2900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Issue2952 extends TestCase {
    public void test_for_issue() throws Exception {
        String expected = "{\"l\":null,\"s\":null,\"b\":null,\"i\":null,\"o\":null}";
        SerializerFeature[] serializerFeatures = {
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullNumberAsZero
        };
        SerializeConfig asmConfig = new SerializeConfig();
        asmConfig.setAsmEnable(true);
        assertEquals(expected, JSON.toJSONString(new Pojo(), asmConfig, serializerFeatures));
        assertEquals(expected, JSON.toJSONString(new Pojo2(), asmConfig, serializerFeatures));
        SerializeConfig noasmConfig = new SerializeConfig();
        noasmConfig.setAsmEnable(false);
        assertEquals(expected, JSON.toJSONString(new Pojo(), noasmConfig, serializerFeatures));
        assertEquals(expected, JSON.toJSONString(new Pojo2(), noasmConfig, serializerFeatures));
    }

    public static class Pojo {
        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, ordinal=0)
        public Object[] l;
        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, ordinal=1)
        public String s;
        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, ordinal=2)
        public Boolean b;
        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, ordinal=3)
        public Integer i;
        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, ordinal=4)
        public Object o;
    }

    @JSONType(serialzeFeatures=SerializerFeature.WriteMapNullValue)
    public static class Pojo2 {
        @JSONField(ordinal=0)
        public Object[] l;
        @JSONField(ordinal=1)
        public String s;
        @JSONField(ordinal=2)
        public Boolean b;
        @JSONField(ordinal=3)
        public Integer i;
        @JSONField(ordinal=4)
        public Object o;
    }
}
