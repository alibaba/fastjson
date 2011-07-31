package com.alibaba.json.test.bvt.parser.deser;

import java.util.UUID;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.UUIDDeserializer;

public class UUIDDeserializerTest extends TestCase {

    public void test_url() throws Exception {
        UUID id = UUID.randomUUID();
        Assert.assertEquals(id, JSON.parseObject("'" + id.toString() + "'", UUID.class));

        Assert.assertEquals(null, JSON.parseObject("null", UUID.class));

        DefaultExtJSONParser parser = new DefaultExtJSONParser("null", ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(null, UUIDDeserializer.instance.deserialze(parser, null));
        Assert.assertEquals(JSONToken.LITERAL_STRING, UUIDDeserializer.instance.getFastMatchToken());

    }

    public void test_url_error() throws Exception {
        JSONException ex = null;
        try {
            JSON.parseObject("'123'", UUID.class);
        } catch (JSONException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }
}
