package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class FeatureCountTest extends TestCase {
    public void testZ_0() throws Exception {
        Feature[] features = Feature.class.getEnumConstants();
        System.out.println(features.length);
        assertTrue(features.length < 32);
    }
}
