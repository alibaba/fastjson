package com.alibaba.json.test.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.TimeZoneDeserializer;

public class TimeZoneDeserializerTest extends TestCase {
    public void test_timezone() throws Exception {
        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, TimeZoneDeserializer.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_STRING, TimeZoneDeserializer.instance.getFastMatchToken());
    }
}
