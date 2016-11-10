package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

/**
 * Created by wenshao on 2016/10/23.
 */
public class Issue868 extends TestCase {
    public void test_int() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Long.MAX_VALUE);
            JSON.parseObject(str, int.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_int_min() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Long.MIN_VALUE);
            JSON.parseObject(str, int.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_short() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Integer.MAX_VALUE);
            JSON.parseObject(str, short.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_short_min() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Integer.MIN_VALUE);
            JSON.parseObject(str, short.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_byte() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Short.MAX_VALUE);
            JSON.parseObject(str, byte.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_byte_min() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Short.MIN_VALUE);
            JSON.parseObject(str, byte.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_float_min() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Double.MIN_VALUE);
            JSON.parseObject(str, float.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_float_max() throws Exception {
        Exception error = null;
        try {
            String str = String.valueOf(Double.MAX_VALUE);
            JSON.parseObject(str, float.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
}
