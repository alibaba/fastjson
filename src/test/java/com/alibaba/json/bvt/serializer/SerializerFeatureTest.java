package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializerFeature;


public class SerializerFeatureTest extends TestCase {
    public void test_0 () throws Exception {
        int feature = 0;
        feature = SerializerFeature.config(feature, SerializerFeature.BrowserCompatible, true);
        Assert.assertEquals(true, SerializerFeature.isEnabled(feature, SerializerFeature.BrowserCompatible));
        feature = SerializerFeature.config(feature, SerializerFeature.BrowserCompatible, false);
        Assert.assertEquals(false, SerializerFeature.isEnabled(feature, SerializerFeature.BrowserCompatible));
    }
    
    public void test_1 () throws Exception {
        int feature = 0;
        feature = SerializerFeature.config(feature, SerializerFeature.BrowserSecure, true);
        Assert.assertEquals(true, SerializerFeature.isEnabled(feature, SerializerFeature.BrowserSecure));
        feature = SerializerFeature.config(feature, SerializerFeature.BrowserSecure, false);
        Assert.assertEquals(false, SerializerFeature.isEnabled(feature, SerializerFeature.BrowserSecure));
    }

    public void test_assert_cnt() throws Exception {
        int len = SerializerFeature.values().length;
        assertTrue(len <= 32);
    }
}
