package com.alibaba.json.bvt.feature;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class FeatureTest_8 extends TestCase {
    public void test_get_obj() throws Exception {
        VO value = JSON.parseObject("{}", VO.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("", value.id);
    }

    private static class VO {
        public String id;
    }
}
