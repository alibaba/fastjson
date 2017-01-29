package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

public class DenyTest5 extends TestCase {

    public void test_c3p0() throws Exception {
        ParserConfig config = new ParserConfig();
        config.setAutoTypeSupport(true);

        Object obj = null;
        Exception error = null;
        try {
            obj = JSON.parseObject("{\"@type\":\"com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase\"}", Object.class, config);
            System.out.println(obj.getClass());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }


}
