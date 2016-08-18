package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.StringCodec;

import junit.framework.TestCase;

public class InetAddressDeserializerTest extends TestCase {

    public void test_null() throws Exception {
        String input = "null";
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        MiscCodec deser = new MiscCodec();

        Assert.assertNull(deser.deserialze(parser, null, null));
    }
    
    public void test_string_null() throws Exception {
        String input = "null";
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        StringCodec deser = new StringCodec();
        
        Assert.assertNull(deser.deserialze(parser, null, null));
    }

    public void test_error_0() throws Exception {
        String input = "'[&中国-^]'";
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        MiscCodec deser = new MiscCodec();

        Throwable error = null;

        Object value = null;
        try {
            value = deser.deserialze(parser, null, null);
        } catch (Throwable ex) {
            error = ex;
        }

        Assert.assertNotNull(error);
    }
}
