package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.MiscCodec;

import junit.framework.TestCase;

public class URIDeserializerTest extends TestCase {

    public void test_null() throws Exception {
        String input = "null";
        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        MiscCodec deser = new MiscCodec();
        Assert.assertEquals(JSONToken.LITERAL_STRING, deser.getFastMatchToken());

        Assert.assertNull(deser.deserialze(parser, null, null));
    }

}
