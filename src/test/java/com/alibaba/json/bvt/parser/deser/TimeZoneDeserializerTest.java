package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.MiscCodec;

import junit.framework.TestCase;

public class TimeZoneDeserializerTest extends TestCase {
    public void test_timezone() throws Exception {
        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, MiscCodec.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_STRING, MiscCodec.instance.getFastMatchToken());
    }
}
