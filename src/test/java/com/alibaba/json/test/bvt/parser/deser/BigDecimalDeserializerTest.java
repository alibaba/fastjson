package com.alibaba.json.test.bvt.parser.deser;

import java.math.BigDecimal;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.BigDecimalDeserializer;

public class BigDecimalDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        Assert.assertEquals(BigDecimal.ZERO, JSON.parseObject("0", BigDecimal.class));
        Assert.assertEquals(BigDecimal.ZERO, JSON.parseObject("'0'", BigDecimal.class));
        Assert.assertEquals(new BigDecimal("0.0"), JSON.parseObject("0.0", BigDecimal.class));
        Assert.assertEquals(new BigDecimal("0.0"), JSON.parseObject("'0.0'", BigDecimal.class));

        Assert.assertEquals(null, JSON.parseObject("null", BigDecimal.class));

        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, BigDecimalDeserializer.instance.deserialze(parser, null));
    }
}
