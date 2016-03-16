package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.JSONArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONObjectDeserializer;
import com.alibaba.fastjson.serializer.ArrayCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.NumberCodec;

import junit.framework.TestCase;

public class FastMatchCheckTest extends TestCase {
    public void test_match() throws Exception {
        Assert.assertEquals(JSONToken.LITERAL_STRING, MiscCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_INT, NumberCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, JSONArrayDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, ArrayCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACE, JSONObjectDeserializer.instance.getFastMatchToken());
    }
}
