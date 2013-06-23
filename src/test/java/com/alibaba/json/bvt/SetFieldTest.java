package com.alibaba.json.bvt;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SetFieldTest extends TestCase {

    public void test_codec() throws Exception {
        V0 v = new V0();
        v.setValue(new HashSet());

        String text = JSON.toJSONString(v);
        System.out.println(text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

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

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
        Assert.assertEquals("{\"value\":[]}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(0, v1.getValue().size());
    }
    
    public void test_codec_null_1_asm() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
        Assert.assertEquals("{\"value\":[]}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(0, v1.getValue().size());
    }

    public static class V0 {

        private Set value;

        public Set getValue() {
            return value;
        }

        public void setValue(Set value) {
            this.value = value;
        }

    }
}
