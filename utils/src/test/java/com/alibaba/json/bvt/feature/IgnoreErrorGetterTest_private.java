package com.alibaba.json.bvt.feature;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class IgnoreErrorGetterTest_private extends TestCase {

    public void test_feature() throws Exception {
        Model model = new Model();
        model.setId(1001);
        String text = JSON.toJSONString(model, SerializerFeature.IgnoreErrorGetter);
        Assert.assertEquals("{\"id\":1001}", text);
    }

    private static class Model {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            throw new IllegalStateException();
        }
    }
}
