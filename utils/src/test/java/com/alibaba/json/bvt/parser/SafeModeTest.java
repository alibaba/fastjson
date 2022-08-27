package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class SafeModeTest extends TestCase {
    public void test_for_safeMode() throws Exception {
        ParserConfig config = new ParserConfig();

        String str = "{\"@type\":\"com.alibaba.json.bvt.parser.SafeModeTest$Entity\"}";
        JSON.parse(str);

        {
            Exception error = null;
            try {
                JSON.parse(str, config, Feature.SafeMode);
            }
            catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }

        {
            Exception error = null;
            try {
                config.setSafeMode(true);
                JSON.parse(str, config);
            }
            catch (JSONException ex) {
                error = ex;
            }
            assertNotNull(error);
        }
    }

    @JSONType
    public static class Entity {

    }
}
