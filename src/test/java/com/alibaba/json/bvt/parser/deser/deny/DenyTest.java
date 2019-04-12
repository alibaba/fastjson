package com.alibaba.json.bvt.parser.deser.deny;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.json.bvtVO.deny.A;

import junit.framework.TestCase;

public class DenyTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{}";

        ParserConfig config = new ParserConfig();

        config.addDeny(null);
        config.addDeny("com.alibaba.json.bvtVO.deny");

        Exception error = null;
        try {
            JSON.parseObject("{\"@type\":\"com.alibaba.json.bvtVO.deny$A\"}", Object.class, config, JSON.DEFAULT_PARSER_FEATURE);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        
        JSON.parseObject(text, B.class, config, JSON.DEFAULT_PARSER_FEATURE);
    }

    public void test_1() throws Exception {
        String text = "{}";

        ParserConfig config = new ParserConfig();

        config.addDeny(null);
        config.addDeny("com.alibaba.json.bvt.parser.deser.deny.DenyTest.B");

        Exception error = null;
        try {
            JSON.parseObject("{\"@type\":\"LLLcom.alibaba.json.bvt.parser.deser.deny.DenyTest$B;;;\"}", Object.class, config, JSON.DEFAULT_PARSER_FEATURE);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);

        JSON.parseObject(text, B.class, config, JSON.DEFAULT_PARSER_FEATURE);
    }

    
  public static class B {
        
    }
}
