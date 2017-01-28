package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

public class DenyTest5 extends TestCase {

    public void test_c3p0() throws Exception {
        Exception error = null;
        try {
            Object obj = JSON.parseObject("{\"@type\":\"com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase\"}", Object.class);
            System.out.println(obj.getClass());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
    
    public static class MyClassLoader extends ClassLoader {

    }

}
