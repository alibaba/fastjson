package com.alibaba.json.bvt.date;

import java.util.Date;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DateFieldTest5 extends TestCase {

    public void test_codec() throws Exception {
        SerializeConfig mapping = new SerializeConfig();

        V0 v = new V0();
        v.setValue(new Date());

        String text = JSON.toJSONString(v, mapping);

        Assert.assertEquals("{\"value\":" + v.getValue().getTime() + "}", text);
    }

    public void test_codec_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new Date());

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + v.getValue().getTime() + "}", text);
    }

    public void test_codec_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new Date());

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + v.getValue().getTime() + "}", text);
    }

    public void test_codec_null_asm() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":null}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }

    public void test_codec_null_1() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero);
        Assert.assertEquals("{\"value\":null}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(null, v1.getValue());
    }

    public static class V0 {

        private Date value;

        @JSONField(format = " ")
        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }

        public boolean is() {
            return true;
        }

        public boolean isa() {
            return true;
        }

        public Object get() {
            return true;
        }

        public Object geta() {
            return true;
        }

        @JSONField(serialize = false)
        public Object getA() {
            return true;
        }
        
        public static Object getB() {
            return true;
        }

    }
}
