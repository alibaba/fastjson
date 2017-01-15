package com.alibaba.json.bvt.parser.deser.doubleDeser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;
import org.junit.Assert;

public class DoubleDeserializerTest extends TestCase {

    public void test_bigdecimal() throws Exception {
        assertEquals(0, JSON.parseObject("0", Double.class).intValue());
        assertEquals(0, JSON.parseObject("0.0", Double.class).intValue());
        assertEquals(0, JSON.parseObject("'0'", Double.class).intValue());

        Assert.assertEquals(null, JSON.parseObject("null", Float.class));

        DefaultJSONParser parser = new DefaultJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }
}

