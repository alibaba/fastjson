package com.alibaba.json.bvt.parser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONArrayDeserializer;
import com.alibaba.fastjson.parser.deserializer.JSONObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimestampDeserializer;
import com.alibaba.fastjson.serializer.AtomicIntegerArrayCodec;
import com.alibaba.fastjson.serializer.AtomicLongArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.FileCodec;
import com.alibaba.fastjson.serializer.InetAddressCodec;
import com.alibaba.fastjson.serializer.InetSocketAddressCodec;
import com.alibaba.fastjson.serializer.LocaleCodec;

public class FastMatchCheckTest extends TestCase {
    public void test_match() throws Exception {
        Assert.assertEquals(JSONToken.LBRACKET, AtomicIntegerArrayCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, AtomicLongArrayCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, InetAddressCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, LocaleCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_INT, NumberDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_INT, TimestampDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, CharsetCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, FileCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, JSONArrayDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACKET, ArrayDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACE, JSONObjectDeserializer.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LBRACE, InetSocketAddressCodec.instance.getFastMatchToken());
        Assert.assertEquals(JSONToken.LITERAL_STRING, CharacterCodec.instance.getFastMatchToken());
    }
}
