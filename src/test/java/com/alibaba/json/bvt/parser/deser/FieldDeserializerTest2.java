package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

public class FieldDeserializerTest2 extends TestCase {

    public void test_0() throws Exception {
        String input = "{,,,\"value\":null,,,,}";
        int featureValues = 0;
        featureValues |= Feature.AllowArbitraryCommas.getMask();
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
        
        Entity object = new Entity();
        parser.parseObject(object);
    }
    
    public void test_1() throws Exception {
        String input = "{,,,\"value\":null,\"id\":123,,,,}";
        int featureValues = 0;
        featureValues |= Feature.AllowArbitraryCommas.getMask();
        featureValues |= Feature.IgnoreNotMatch.getMask();
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
        
        Entity object = new Entity();
        parser.parseObject(object);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            String input = "{\"value\":null,\"id\":123}";
            int featureValues = 0;
            DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
            
            Entity object = new Entity();
            parser.parseObject(object);
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
