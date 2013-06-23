package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ObjectFieldTest extends TestCase {

    public void test_codec_null() throws Exception {

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        {
            V0 v = new V0();
            String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
            Assert.assertEquals("{\"value\":null}", text);

            V0 v1 = JSON.parseObject(text, V0.class);

            Assert.assertEquals(v1.getValue(), v.getValue());
        }
        {
            V0 v = new V0();
            v.setValue(Integer.valueOf(123));
            String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
            Assert.assertEquals("{\"value\":123}", text);

            V0 v1 = JSON.parseObject(text, V0.class);

            Assert.assertEquals(v1.getValue(), v.getValue());
        }
    }

    public void test_codec_null_1() throws Exception {

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        {
            V0 v = new V0();

            String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
            Assert.assertEquals("{\"value\":null}", text);
        }
        {
            V0 v = new V0();
            v.setValue(Integer.valueOf(123));
            String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
            Assert.assertEquals("{\"value\":123}", text);

            V0 v1 = JSON.parseObject(text, V0.class);

            Assert.assertEquals(v1.getValue(), v.getValue());
        }
    }

    public static class V0 {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }
}
