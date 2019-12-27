package com.alibaba.json.bvt.issue_2900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
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
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.setAsmEnable(true);
        assertEquals(expected, JSON.toJSONString(new Pojo(), serializeConfig, serializerFeatures));
        serializeConfig.setAsmEnable(false);
        assertEquals(expected, JSON.toJSONString(new Pojo(), serializeConfig, serializerFeatures));
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
}
