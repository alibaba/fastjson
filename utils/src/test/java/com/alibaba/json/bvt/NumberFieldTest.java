package com.alibaba.json.bvt;

import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class NumberFieldTest extends TestCase {

    public void test_codec() throws Exception {
        V0 v = new V0();
        v.setValue(1001L);

        String text = JSON.toJSONString(v);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(v1.getValue().intValue(), v.getValue().intValue());
    }
    
    public void test_codec_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(1001L);
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":1001}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(Integer.valueOf(1001), v1.getValue());
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
    
    public void test_codec_2_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + Long.MAX_VALUE + "}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(new Long(Long.MAX_VALUE), v1.getValue());
    }
    
    public void test_codec_2_asm() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MAX_VALUE);
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + Long.MAX_VALUE + "}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(new Long(Long.MAX_VALUE), v1.getValue());
    }
    
    public void test_codec_3_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new BigDecimal("3.2"));
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":3.2}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(new BigDecimal("3.2"), v1.getValue());
    }
    
    public void test_codec_3_asm() throws Exception {
        V0 v = new V0();
        v.setValue(new BigDecimal("3.2"));
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);
        
        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":3.2}", text);
        
        V0 v1 = JSON.parseObject(text, V0.class);
        
        Assert.assertEquals(new BigDecimal("3.2"), v1.getValue());
    }
    
    public void test_codec_min_no_asm() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + Long.MIN_VALUE + "}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(new Long(Long.MIN_VALUE), v1.getValue());
    }
    
    public void test_codec_min_asm() throws Exception {
        V0 v = new V0();
        v.setValue(Long.MIN_VALUE);
        
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);
        
        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"value\":" + Long.MIN_VALUE + "}", text);
        
        V0 v1 = JSON.parseObject(text, V0.class);
        
        Assert.assertEquals(new Long(Long.MIN_VALUE), v1.getValue());
    }

    public void test_codec_null_1() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero);
        Assert.assertEquals("{\"value\":0}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(Integer.valueOf(0), v1.getValue());
    }
    
    public void test_codec_null_1_asm() throws Exception {
        V0 v = new V0();

        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(true);

        String text = JSON.toJSONString(v, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero);
        Assert.assertEquals("{\"value\":0}", text);

        V0 v1 = JSON.parseObject(text, V0.class);

        Assert.assertEquals(Integer.valueOf(0), v1.getValue());
    }
    
    public void test_codec_cast() throws Exception {

        V0 v1 = JSON.parseObject("{\"value\":\"12.3\"}", V0.class);

        Assert.assertEquals(new BigDecimal("12.3"), v1.getValue());
    }

    public static class V0 {

        private Number value;

        public Number getValue() {
            return value;
        }

        public void setValue(Number value) {
            this.value = value;
        }

    }
}
