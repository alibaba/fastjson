package com.alibaba.json.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;

public class FieldDeserializerTest4 extends TestCase {


    public void test_error_0() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{", ParserConfig.getGlobalInstance(), featureValues);
        parser.config(Feature.AllowUnQuotedFieldNames, true);

        Entity object = new Entity();
        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        Exception error = null;
        try {
        objectDeser.parseObject(parser, object);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        int featureValues = 0;
        DefaultExtJSONParser parser = new DefaultExtJSONParser("{\"value\":null", ParserConfig.getGlobalInstance(), featureValues);
        parser.config(Feature.AllowUnQuotedFieldNames, true);

        Entity object = new Entity();
        DefaultObjectDeserializer objectDeser = new DefaultObjectDeserializer();
        Exception error = null;
        try {
        objectDeser.parseObject(parser, object);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Entity {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
