package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import java.util.Properties;

public class DenyTest8 extends TestCase {

    public void test_autoTypeDeny() throws Exception {
        ParserConfig config = new ParserConfig();

        assertFalse(config.isAutoTypeSupport());
        config.setAutoTypeSupport(true);
        assertTrue(config.isAutoTypeSupport());

        Properties properties = new Properties();
        properties.put(ParserConfig.AUTOTYPE_SUPPORT_PROPERTY, "false");
        config.configFromPropety(properties);

        assertFalse(config.isAutoTypeSupport());

        config.addAccept("com.alibaba.json.bvt.parser.deser.deny.DenyTest8");


        Object obj = JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.deny.DenyTest8$Model\"}", Object.class, config);
        assertEquals(Model.class, obj.getClass());
    }
    
    public static class Model {

    }

}
