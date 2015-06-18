package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.URICodec;

public class URIDeserializerTest extends TestCase {

    public void test_null() throws Exception {
        String input = "null";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        URICodec deser = new URICodec();
        Assert.assertEquals(JSONToken.LITERAL_STRING, deser.getFastMatchToken());

        Assert.assertNull(deser.deserialze(parser, null, null));
    }

}
