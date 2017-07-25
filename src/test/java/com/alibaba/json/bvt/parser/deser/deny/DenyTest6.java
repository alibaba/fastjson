package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class DenyTest6 extends TestCase {

    public void test_autoTypeDeny() throws Exception {
        ParserConfig config = new ParserConfig();
        assertFalse(config.isAutoTypeSupport());
        config.setAutoTypeSupport(true);
        assertTrue(config.isAutoTypeSupport());
        config.addDeny("com.alibaba.json.bvt.parser.deser.deny.DenyTest6");
        config.setAutoTypeSupport(false);

        Exception error = null;
        try {
            Object obj = JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.deny.DenyTest6$Model\"}", Object.class, config);
            System.out.println(obj.getClass());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
    
    public static class Model {

    }

}
