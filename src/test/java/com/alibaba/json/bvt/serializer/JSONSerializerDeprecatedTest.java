package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JSONSerializerMap;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

@SuppressWarnings("deprecation")
public class JSONSerializerDeprecatedTest extends TestCase {

    public void test_() throws Exception {
        JSONSerializer ser = new JSONSerializer(new JSONSerializerMap());
        
        ser.setDateFormat(new ISO8601DateFormat());
        Assert.assertEquals(null, ser.getDateFormatPattern());
        
        ser.close();
    }
}
