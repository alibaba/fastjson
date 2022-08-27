package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

public class WriteNonStringValueAsStringTestFloatField2 extends TestCase {
    public void test_0() throws Exception {
        VO vo = new VO();
        vo.id = 100;
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteNonStringValueAsString);
         Assert.assertEquals("{\"id\":\"100.00\"}", text);
    }
    
    public static class VO {
        @JSONField(format = "0.00")
        public float id;
    }
}
