package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.serializer.CharacterCodec;

public class TestNull extends TestCase {

    public void test_byte() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
    
    public void test_char() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new CharacterCodec().deserialze(parser, null, null));
    }
    
    public void test_short() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
    
    public void test_null() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
}
