package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.NumberCodec;

import junit.framework.TestCase;

public class TestNull extends TestCase {

    public void test_byte() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertNull(NumberCodec.instance.deserialze(parser, null, null));
    }
    
    public void test_char() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(NumberCodec.instance.deserialze(parser, null, null));
    }
    
    public void test_short() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(NumberCodec.instance.deserialze(parser, null, null));
    }
    
    public void test_null() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(NumberCodec.instance.deserialze(parser, null, null));
    }
}
