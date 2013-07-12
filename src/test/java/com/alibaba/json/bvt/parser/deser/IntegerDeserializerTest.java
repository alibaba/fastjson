package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.IntegerDeserializer;

public class IntegerDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        Assert.assertEquals(0, JSON.parseObject("0", Integer.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("0.0", Integer.class).intValue());
        Assert.assertEquals(0, JSON.parseObject("'0'", Integer.class).intValue());

        Assert.assertEquals(null, JSON.parseObject("null", Integer.class));

        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, IntegerDeserializer.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_INT, IntegerDeserializer.instance.getFastMatchToken());
    }
}
