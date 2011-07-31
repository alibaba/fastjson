package com.alibaba.json.test.bvt.parser.deser;

import java.math.BigInteger;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.BigIntegerDeserializer;

public class BigIntegerDeserializerTest extends TestCase {
    public void test_null() throws Exception {
        String input = "null";
        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);

        BigIntegerDeserializer deser = new BigIntegerDeserializer();

        Assert.assertNull(deser.deserialze(parser, null));
    }
    
    public void test_1() throws Exception {

        BigInteger value = JSON.parseObject("'123'", BigInteger.class);

        Assert.assertEquals(new BigInteger("123"), value);
    }
}
