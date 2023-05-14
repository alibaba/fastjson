package com.alibaba.json.bvt.serializer.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 29/01/2017.
 */
public class ExceptionTest extends TestCase {
    public void test_exception() throws Exception {
        ParserConfig config = new ParserConfig();

        IllegalAccessError ex = new IllegalAccessError();

        String text = JSON.toJSONString(ex);

        JSON.parseObject(text, IllegalAccessError.class, config);
        JSON.parseObject(text, IllegalAccessError.class, config, Feature.SupportAutoType);
        assertEquals(IllegalAccessError.class, JSON.parseObject(text, Exception.class, config, Feature.SupportAutoType).getClass());

        assertEquals(
                JSONObject.class,
                JSON
                        .parse(text, config)
                        .getClass());

        assertTrue(JSON.parse(text, config, Feature.SupportAutoType) instanceof IllegalAccessError);
    }
}
