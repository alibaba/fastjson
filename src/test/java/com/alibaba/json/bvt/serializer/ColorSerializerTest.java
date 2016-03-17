package com.alibaba.json.bvt.serializer;

import java.awt.Color;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class ColorSerializerTest extends TestCase {

    public void test_null() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(AwtCodec.class, serializer.getObjectWriter(Color.class).getClass());

        VO vo = new VO();

        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }

    public void test_rgb() throws Exception {
        JSONSerializer serializer = new JSONSerializer();
        Assert.assertEquals(AwtCodec.class, serializer.getObjectWriter(Color.class).getClass());

        VO vo = new VO();
        vo.setValue(new Color(1,1,1,0));

        Assert.assertEquals("{\"value\":{\"r\":1,\"g\":1,\"b\":1}}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }
    
    public void test_rgb_getAutowiredFor() throws Exception {
        
    }

    private static class VO {

        private Color value;

        public Color getValue() {
            return value;
        }

        public void setValue(Color value) {
            this.value = value;
        }

    }
}
