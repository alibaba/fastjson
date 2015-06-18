package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.InetAddressCodec;
import com.alibaba.fastjson.serializer.StringCodec;

public class InetAddressDeserializerTest extends TestCase {

    public void test_null() throws Exception {
        String input = "null";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        InetAddressCodec deser = new InetAddressCodec();

        Assert.assertNull(deser.deserialze(parser, null, null));
    }
    
    public void test_string_null() throws Exception {
        String input = "null";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        
        StringCodec deser = new StringCodec();
        
        Assert.assertNull(deser.deserialze(parser, null, null));
    }

    public void test_error_0() throws Exception {
        String input = "'[&中国-^]'";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        InetAddressCodec deser = new InetAddressCodec();

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
