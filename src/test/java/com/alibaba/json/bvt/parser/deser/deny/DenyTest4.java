package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class DenyTest4 extends TestCase {

    public void test_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.deser.deny.DenyTest4$MyClassLoader\"}", Object.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
    
    public static class MyClassLoader extends ClassLoader {

    }

}
