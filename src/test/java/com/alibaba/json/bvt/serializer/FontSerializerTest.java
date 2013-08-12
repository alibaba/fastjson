package com.alibaba.json.bvt.serializer;

import java.awt.Font;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.FontCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class FontSerializerTest extends TestCase {
    
    public void test_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(FontCodec.class, serializer.getObjectWriter(Font.class).getClass());
        
        VO vo = new VO();
        
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }

    private static class VO {

        private Font value;

        public Font getValue() {
            return value;
        }

        public void setValue(Font value) {
            this.value = value;
        }

    }
}
