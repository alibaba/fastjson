package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;
import org.junit.Assert;

public class DenyTest extends TestCase {
    public void test_0() throws Exception {
        String text = "{}";

        ParserConfig config = new ParserConfig();

        config.addDeny(null);
        config.addDeny("com.alibaba.json.bvtVO.deny");

        Exception error = null;
        try {
            Object obj = JSON.parseObject("{\"@type\":\"com.alibaba.json.bvtVO.deny$A\"}", Object.class, config, JSON.DEFAULT_PARSER_FEATURE);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        
        JSON.parseObject(text, B.class, config, JSON.DEFAULT_PARSER_FEATURE);
    }

    
    public static class B {
        
    }
}
