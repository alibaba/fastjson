package com.alibaba.json.bvt.parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest_cast extends TestCase {

    public void test_cast_0() throws Exception {
        Assert.assertArrayEquals(new byte[0], TypeUtils.cast(new byte[0], byte[].class, null));
    }
    
    public void test_cast_1() throws Exception {
        ParameterizedType parameterizedType = (ParameterizedType) new TypeReference<List<?>>() {}.getType();
        Type type = parameterizedType.getActualTypeArguments()[0];
        Assert.assertEquals(null, TypeUtils.cast("", type, null));
    }

    public void test_castToDate_error() throws Exception {
        Exception error = null;
        try {
            TypeUtils.cast(0, MyCalendar.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_castToDate_error_nullClass() throws Exception {
        Exception error = null;
        try {
            TypeUtils.cast(0, (Class<?>) null, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    @SuppressWarnings("serial")
    private class MyCalendar extends Calendar {

        @Override
        protected void computeTime() {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected void computeFields() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void add(int field, int amount) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void roll(int field, boolean up) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public int getMinimum(int field) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getMaximum(int field) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getGreatestMinimum(int field) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getLeastMaximum(int field) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}
