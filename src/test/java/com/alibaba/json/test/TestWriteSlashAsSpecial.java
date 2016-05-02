package com.alibaba.json.test;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

@SuppressWarnings("deprecation")
public class TestWriteSlashAsSpecial extends TestCase {


    public void test_writeSlashAsSpecial() throws Exception {
        int features = JSON.DEFAULT_GENERATE_FEATURE;
        features = SerializerFeature.config(features, SerializerFeature.WriteSlashAsSpecial, true);
        features = SerializerFeature.config(features, SerializerFeature.WriteTabAsSpecial, true);
        features = SerializerFeature.config(features, SerializerFeature.DisableCircularReferenceDetect, true);
        features = SerializerFeature.config(features, SerializerFeature.SortField, false);

        Assert.assertEquals("\"\\/\"", JSON.toJSONString("/", features));
    }
}
