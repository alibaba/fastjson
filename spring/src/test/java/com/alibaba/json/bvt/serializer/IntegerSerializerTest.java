package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class IntegerSerializerTest extends TestCase {
    
    public void test_null() throws Exception {
        VO vo = new VO();
        
        Assert.assertEquals("{\"value\":null}", JSON.toJSONString(vo, SerializerFeature.WriteMapNullValue));
    }

    private static class VO {

        private Integer value;

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

    }
}
