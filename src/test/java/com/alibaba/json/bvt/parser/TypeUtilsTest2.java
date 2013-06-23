package com.alibaba.json.bvt.parser;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest2 extends TestCase {

    public void test_0() throws Exception {
        Assert.assertNull(TypeUtils.cast("", Entity.class, null));
        Assert.assertNull(TypeUtils.cast("", Type.class, null));
        Assert.assertNull(TypeUtils.cast("", Byte.class, null));
        Assert.assertNull(TypeUtils.cast("", Short.class, null));
        Assert.assertNull(TypeUtils.cast("", Integer.class, null));
        Assert.assertNull(TypeUtils.cast("", Long.class, null));
        Assert.assertNull(TypeUtils.cast("", Float.class, null));
        Assert.assertNull(TypeUtils.cast("", Double.class, null));
        Assert.assertNull(TypeUtils.cast("", Character.class, null));
        Assert.assertNull(TypeUtils.cast("", java.util.Date.class, null));
        Assert.assertNull(TypeUtils.cast("", java.sql.Date.class, null));
        Assert.assertNull(TypeUtils.cast("", java.sql.Timestamp.class, null));

        Assert.assertNull(TypeUtils.castToChar(""));
        Assert.assertNull(TypeUtils.castToChar(null));
        Assert.assertEquals('A', TypeUtils.castToChar('A').charValue());
        Assert.assertEquals('A', TypeUtils.castToChar("A").charValue());

        Assert.assertNull(TypeUtils.castToBigDecimal(""));
        Assert.assertNull(TypeUtils.castToBigInteger(""));
        Assert.assertNull(TypeUtils.castToBoolean(""));
        Assert.assertNull(TypeUtils.castToEnum("", Type.class, null));
        
        Assert.assertEquals(null, TypeUtils.cast("", new TypeReference<Pair<Object, Object>>() {

        }.getType(), null));
    }

    public void test_1() throws Exception {
        Assert.assertEquals(null, TypeUtils.cast("", new TypeReference<List<Object>>() {

        }.getType(), null));

    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            Assert.assertEquals(null, TypeUtils.cast("a", new TypeReference<List<Object>>() {

            }.getType(), null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            Assert.assertEquals(null, TypeUtils.cast("a", new TypeReference<Pair<Object, Object>>() {

            }.getType(), null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            Assert.assertEquals(null, TypeUtils.cast("a", ((ParameterizedType) new TypeReference<List<?>>() {

            }.getType()).getActualTypeArguments()[0], null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToChar("abc");
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToChar(true);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class Entity {

    }

    public static class Pair<K, V> {

    }

    public static enum Type {
        A, B, C
    }

}
