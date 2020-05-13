package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;

public class DoubleDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        Assert.assertEquals(0, JSON.parseObject("0", Double.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("0.0", Double.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("'0'", Double.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("'0'", double.class).intValue());
        Assert.assertEquals(null, JSON.parseObject("null", Double.class));

        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, NumberDeserializer.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_INT, NumberDeserializer.instance.getFastMatchToken());
    }
}

