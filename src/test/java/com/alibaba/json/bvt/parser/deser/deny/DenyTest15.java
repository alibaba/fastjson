package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 29/01/2017.
 */
public class DenyTest15 extends TestCase {
    public void test_deny() throws Exception {
        String text = "{\"value\":{\"@type\":\"com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase\"}}";
        Exception error = null;
        try {
            Model model = JSON.parseObject(text, Model.class);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public static class Model {
        public Throwable value;
    }
}
