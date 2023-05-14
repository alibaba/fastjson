package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class AppendableFieldTest extends TestCase {

    public void test_codec_null() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":null}", text);
    }

    public void test_codec_null_1() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        Assert.assertTrue(!mapping.isAsmEnable());

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
        Assert.assertEquals("{\"value\":\"\"}", text);
    }

    public static class V0 {

        private Appendable value;

        public Appendable getValue() {
            return value;
        }

        public void setValue(Appendable value) {
            this.value = value;
        }

    }
}
