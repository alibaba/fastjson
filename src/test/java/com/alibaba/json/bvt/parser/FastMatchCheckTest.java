package com.alibaba.json.bvt.parser;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;

import junit.framework.TestCase;

public class FastMatchCheckTest extends TestCase {
    public void test_match() throws Exception {
        Assert.assertEquals(JSONToken.LBRACKET, AtomicCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, MiscCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_INT, NumberDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_INT, SqlDateDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, CharsetCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, ObjectArrayCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, CharacterCodec.instance.getFastMatchToken());
    }
}
