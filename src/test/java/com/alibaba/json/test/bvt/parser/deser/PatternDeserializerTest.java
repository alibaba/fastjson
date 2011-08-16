package com.alibaba.json.test.bvt.parser.deser;

import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.PatternDeserializer;

public class PatternDeserializerTest extends TestCase {

    public void test_pattern() throws Exception {
        Assert.assertEquals(Pattern.compile("abc").pattern(), JSON.parseObject("'abc'", Pattern.class).pattern());

        Assert.assertEquals(null, JSON.parseObject("null", Pattern.class));

        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, PatternDeserializer.instance.deserialze(parser, null, null));
        Assert.assertEquals(JSONToken.LITERAL_STRING, PatternDeserializer.instance.getFastMatchToken());
    }
}
