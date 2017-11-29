package com.alibaba.json.bvt.parser.deser;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.BigDecimalCodec;

public class BigDecimalDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        Assert.assertEquals(BigDecimal.ZERO, JSON.parseObject("0", BigDecimal.class));
        Assert.assertEquals(BigDecimal.ZERO, JSON.parseObject("'0'", BigDecimal.class));
        Assert.assertEquals(new BigDecimal("0.0"), JSON.parseObject("0.0", BigDecimal.class));
        Assert.assertEquals(new BigDecimal("0.0"), JSON.parseObject("'0.0'", BigDecimal.class));

        Assert.assertEquals(null, JSON.parseObject("null", BigDecimal.class));

        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, BigDecimalCodec.instance.deserialze(parser, null, null));
    }
}
