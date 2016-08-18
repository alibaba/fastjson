package com.alibaba.json.bvt.serializer.features;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteNonStringValueAsStringTestByteObjectField extends TestCase {
    public void test_0() throws Exception {
        VO vo = new VO();
        vo.id = 100;
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteNonStringValueAsString);
         Assert.assertEquals("{\"id\":\"100\"}", text);
    }
    
    public void test_1() throws Exception {
        V1 vo = new V1();
        vo.id = 100;
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteNonStringValueAsString);
         Assert.assertEquals("{\"id\":\"100\"}", text);
    }
    
    public static class VO {
        public Byte id;
    }
    
    
    private static class V1 {
        public Byte id;
    }
}
