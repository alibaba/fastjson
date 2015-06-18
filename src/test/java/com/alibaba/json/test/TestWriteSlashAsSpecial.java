package com.alibaba.json.test;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings("deprecation")
public class TestWriteSlashAsSpecial extends TestCase {

    private int defaultValue = JSON.DEFAULT_GENERATE_FEATURE;

    protected void setUp() throws Exception {
        defaultValue = JSON.DEFAULT_GENERATE_FEATURE;
    }

    protected void tearDown() throws Exception {
        JSON.DEFAULT_GENERATE_FEATURE = defaultValue;
    }

    public void test_writeSlashAsSpecial() throws Exception {
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE,
                                                                 SerializerFeature.WriteSlashAsSpecial, true);
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE,
                                                                 SerializerFeature.WriteTabAsSpecial, true);
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE,
                                                                 SerializerFeature.DisableCircularReferenceDetect, true);
        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE,
                                                                 SerializerFeature.SortField, false);

        Assert.assertEquals("\"\\/\"", JSON.toJSONString("/"));
    }
}
