package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import java.util.Properties;

public class DenyTest7 extends TestCase {

    public void test_autoTypeDeny() throws Exception {
        ParserConfig config = new ParserConfig();

        assertFalse(config.isAutoTypeSupport());
        config.setAutoTypeSupport(true);
        assertTrue(config.isAutoTypeSupport());

        Properties properties = new Properties();
        properties.put(ParserConfig.AUTOTYPE_SUPPORT_PROPERTY, "false");

        config.configFromPropety(properties);

        assertFalse(config.isAutoTypeSupport());

        Exception error = null;
        try {
            Object obj = JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.deny.DenyTest7$Model\"}", Object.class, config);
            System.out.println(obj.getClass());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
    
    public static class Model {

    }

}
