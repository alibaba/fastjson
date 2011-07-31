package com.alibaba.json.test.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ByteDeserializer;
import com.alibaba.fastjson.parser.deserializer.CharacterDeserializer;
import com.alibaba.fastjson.parser.deserializer.ShortDeserializer;

public class TestNull extends TestCase {

    public void test_byte() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        Assert.assertNull(new ByteDeserializer().deserialze(parser, null));
    }
    
    public void test_char() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new CharacterDeserializer().deserialze(parser, null));
    }
    
    public void test_short() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new ShortDeserializer().deserialze(parser, null));
    }
    
    public void test_null() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        Assert.assertNull(new ShortDeserializer().deserialze(parser, null));
    }
}
