package com.alibaba.json.bvt.parser;

import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest3 extends TestCase {

    public void test_enum() throws Exception {
        Assert.assertEquals(Type.A, JSON.parseObject("\"A\"", Type.class));
        Assert.assertEquals(Type.A, JSON.parseObject("" + Type.A.ordinal(), Type.class));
        Assert.assertEquals(Type.B, JSON.parseObject("" + Type.B.ordinal(), Type.class));
        Assert.assertEquals(Type.C, JSON.parseObject("" + Type.C.ordinal(), Type.class));
    }

    public void test_enum_2() throws Exception {
        Assert.assertEquals(Type.A, TypeUtils.cast("A", Type.class, null));
        Assert.assertEquals(Type.A, TypeUtils.cast(Type.A.ordinal(), Type.class, null));
        Assert.assertEquals(Type.B, TypeUtils.cast(Type.B.ordinal(), Type.class, null));
    }

    public void test_error() throws Exception {
        assertNull(TypeUtils.castToEnum("\"A1\"", Type.class, null));
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToEnum(Boolean.TRUE, Type.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToEnum(1000, Type.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_null() throws Exception {
        Assert.assertEquals(null, TypeUtils.cast(null, new TypeReference<Object>() {
        }.getType(), null));
    }
    
    public void test_null_1() throws Exception {
        Assert.assertEquals(null, TypeUtils.cast("", new TypeReference<List<Object>>() {
        }.getType(), null));
    }
    
    public void test_null_2() throws Exception {
        Assert.assertEquals(null, TypeUtils.cast("", new TypeReference<Object[]>() {
        }.getType(), null));
    }
    
    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            TypeUtils.cast("xxx", new TypeReference<Object[]>() {
            }.getType(), null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    
    public void test_ex() throws Exception {
        RuntimeException ex = new RuntimeException();
        JSONObject object = (JSONObject) JSON.toJSON(ex);
        JSONArray array = object.getJSONArray("stackTrace");
        array.getJSONObject(0).put("lineNumber", null);
        
        JSON.parseObject(object.toJSONString(), Exception.class);
    }

    public static enum Type {
        A, B, C
    }
}
