package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.serializer.CharacterCodec;

public class TestNull extends TestCase {

    public void test_byte() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
    
    public void test_char() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new CharacterCodec().deserialze(parser, null, null));
    }
    
    public void test_short() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
    
    public void test_null() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new NumberDeserializer().deserialze(parser, null, null));
    }
}
