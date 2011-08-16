package com.alibaba.json.test.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.DoubleDeserializer;

public class DoubleDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        Assert.assertEquals(0, JSON.parseObject("0", Double.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("0.0", Double.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("'0'", Double.class).intValue());

        Assert.assertEquals(null, JSON.parseObject("null", Double.class));

        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, DoubleDeserializer.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_INT, DoubleDeserializer.instance.getFastMatchToken());
    }
}

