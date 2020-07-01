package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class AutoTypeCheckHandlerTest extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig config = new ParserConfig();

        String str = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeCheckHandlerTest$Model\"}";
        JSONException error = null;
        try {
            JSON.parse(str, config);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);

        config.addAutoTypeCheckHandler(new ParserConfig.AutoTypeCheckHandler() {
            @Override
            public Class<?> handler(String typeName, Class<?> expectClass, int features) {
                if ("com.alibaba.json.bvt.parser.autoType.AutoTypeCheckHandlerTest$Model".equals(typeName)) {
                    return Model.class;
                }
                return null;
            }
        });

        JSON.parse(str, config);
    }

    public static class Model {

    }
}
