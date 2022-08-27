package com.alibaba.json.bvt.feature;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class IgnoreErrorGetterTest extends TestCase {

    public void test_feature() throws Exception {
        Model model = new Model();
        model.id = 1001;
        String text = JSON.toJSONString(model, SerializerFeature.IgnoreErrorGetter);
        Assert.assertEquals("{\"id\":1001}", text);
    }

    public static class Model {
        public int id;
    }
}
