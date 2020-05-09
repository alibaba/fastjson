package com.alibaba.json.bvt.serializer;

import java.awt.Rectangle;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;


public class RectangleSerializerTest extends TestCase {
    
    public void test_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(AwtCodec.class, serializer.getObjectWriter(Rectangle.class).getClass());
        
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
