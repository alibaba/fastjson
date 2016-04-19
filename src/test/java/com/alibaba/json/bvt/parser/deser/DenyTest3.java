package com.alibaba.json.bvt.parser.deser;

import java.util.Properties;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.json.bvtVO.deny.A;

import junit.framework.TestCase;

public class DenyTest3 extends TestCase {

    public void test_0() throws Exception {
        String text = "{}";

        ParserConfig config = new ParserConfig();

        Properties properties = new Properties();
        properties.put(ParserConfig.DENY_PROPERTY, "com.alibaba.json.bvtVO.deny,,aa");
        config.configFromPropety(properties);
        
        Exception error = null;
        try {
            JSON.parseObject(text, A.class, config, JSON.DEFAULT_PARSER_FEATURE);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        
        JSON.parseObject(text, B.class, config, JSON.DEFAULT_PARSER_FEATURE);
    }
    
    public static class B {
        
    }

}
