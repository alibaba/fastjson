package com.alibaba.json.bvt.serializer.exception;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 29/01/2017.
 */
public class ExceptionTest extends TestCase {
    public void test_exception() throws Exception {
        IllegalAccessError ex = new IllegalAccessError();

        String text = JSON.toJSONString(ex);
        assertTrue(JSON.parse(text) instanceof IllegalAccessError);
    }
}
