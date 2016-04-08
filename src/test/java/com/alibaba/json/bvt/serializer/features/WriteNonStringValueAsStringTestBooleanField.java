package com.alibaba.json.bvt.serializer.features;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class WriteNonStringValueAsStringTestBooleanField extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.id = true;

        Assert.assertEquals("{\"id\":\"true\"}", JSON.toJSONString(vo, SerializerFeature.WriteNonStringValueAsString));
        Assert.assertEquals("{\"id\":true}", JSON.toJSONString(vo));
    }

    public static class VO {

        public boolean id;
    }
}
