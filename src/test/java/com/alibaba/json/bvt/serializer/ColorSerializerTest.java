package com.alibaba.json.bvt.serializer;

import java.awt.Color;
import java.awt.Rectangle;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ColorSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class ColorSerializerTest extends TestCase {
    
    public void test_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(ColorSerializer.class, serializer.getObjectWriter(Color.class).getClass());
        
        VO vo = new VO();
        
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }

    private static class VO {

        private Rectangle value;

        public Rectangle getValue() {
            return value;
        }

        public void setValue(Rectangle value) {
            this.value = value;
        }

    }
}
