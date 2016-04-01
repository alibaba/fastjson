package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializerFeature;


public class SerializerFeatureTest extends TestCase {
    public void test_0 () throws Exception {
        int feature = 0;
        feature = config(feature, SerializerFeature.BrowserCompatible, true);
        Assert.assertEquals(true, isEnabled(feature, SerializerFeature.BrowserCompatible));
        feature = config(feature, SerializerFeature.BrowserCompatible, false);
        Assert.assertEquals(false, isEnabled(feature, SerializerFeature.BrowserCompatible));
    }
    
    public static boolean isEnabled(int features, SerializerFeature feature) {
        return (features & feature.mask) != 0;
    }
    
    public static int config(int features, SerializerFeature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }

        return features;
    }
}
