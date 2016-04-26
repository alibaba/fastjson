package com.alibaba.json.bvt.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class DateFieldTest3 extends TestCase {
    protected void setUp() throws Exception {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;
    }
    
    public void test_codec() throws Exception {
        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));

        V0 v = new V0();
        v.setValue(new Date());

        String text = JSON.toJSONString(v, mapping);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals("{\"value\":" + JSON.toJSONString(format.format(v.getValue())) + "}", text);
    }

    public void test_codec_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new Date());

        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals("{\"value\":" + JSON.toJSONString(format.format(v.getValue())) + "}", text);
    }
    
    public void test_codec_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new Date());
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        mapping.setAsmEnable(true);
        
        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", JSON.defaultLocale);
        format.setTimeZone(JSON.defaultTimeZone);
        Assert.assertEquals("{\"value\":" + JSON.toJSONString(format.format(v.getValue())) + "}", text);
    }

    public void test_codec_null_asm() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
        Assert.assertEquals("{\"value\":null}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue(), v.getValue());
    }
    
    public void test_codec_null_no_asm() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
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

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero);
        Assert.assertEquals("{\"value\":null}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(null, v1.getValue());
    }

    public static class V0 {

        private Date value;

        public Date getValue() {
            return value;
        }

        public void setValue(Date value) {
            this.value = value;
        }

    }
}
