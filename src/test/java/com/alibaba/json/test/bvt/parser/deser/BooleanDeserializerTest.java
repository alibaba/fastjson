package com.alibaba.json.test.bvt.parser.deser;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.BooleanDeserializer;

public class BooleanDeserializerTest extends TestCase {

    public void test_boolean() throws Exception {
        Assert.assertEquals(Boolean.TRUE, JSON.parseObject("true", Boolean.class));
        Assert.assertEquals(Boolean.FALSE, JSON.parseObject("false", Boolean.class));

        Assert.assertEquals(Boolean.TRUE, JSON.parseObject("'true'", Boolean.class));
        Assert.assertEquals(Boolean.FALSE, JSON.parseObject("'false'", Boolean.class));

        Assert.assertEquals(Boolean.TRUE, JSON.parseObject("1", Boolean.class));
        Assert.assertEquals(Boolean.FALSE, JSON.parseObject("0", Boolean.class));

        Assert.assertEquals(null, JSON.parseObject("null", Boolean.class));

        {
            DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
            Assert.assertEquals(null, BooleanDeserializer.instance.deserialze(parser, null));
        }
    }
}
