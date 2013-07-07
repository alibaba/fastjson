package com.alibaba.json.bvt.serializer;

import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;

public class JSONSerializerTest3 extends TestCase {

    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.setDateFormat("yyyy");
        Assert.assertEquals("yyyy", ((SimpleDateFormat) serializer.getDateFormat()).toPattern());
        Assert.assertEquals("yyyy",  serializer.getDateFormatPattern());
        
        serializer.setDateFormat("yyyy-MM");
        Assert.assertEquals("yyyy-MM", ((SimpleDateFormat) serializer.getDateFormat()).toPattern());
        
        serializer.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        Assert.assertEquals("yyyy-MM-dd",  serializer.getDateFormatPattern());
    }

}
