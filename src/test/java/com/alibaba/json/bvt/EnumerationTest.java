package com.alibaba.json.bvt;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EnumerationTest extends TestCase {

    public void test_enumeration() throws Exception {
        Assert.assertEquals("[]", JSON.toJSONString(new Vector().elements()));
        Assert.assertEquals("[null]", JSON.toJSONString(new Vector(Collections.singleton(null)).elements()));
        
        Assert.assertEquals("{\"value\":[]}", JSON.toJSONString(new VO(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
    }

    private static class VO {

        private Enumeration value;

        public Enumeration getValue() {
            return value;
        }

        public void setValue(Enumeration value) {
            this.value = value;
        }

    }
}
