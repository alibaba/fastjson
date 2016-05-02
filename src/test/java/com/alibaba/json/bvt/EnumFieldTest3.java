package com.alibaba.json.bvt;

import java.io.StringWriter;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class EnumFieldTest3 extends TestCase {

    public void test_1() throws Exception {
        Model[] array = new Model[2048];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new Model();
            array[i].value = Type.A;
        }

        String text = JSON.toJSONString(array);

        Model[] array2 = JSON.parseObject(text, Model[].class);

        Assert.assertEquals(array.length, array2.length);
        for (int i = 0; i < array.length; ++i) {
            Assert.assertEquals(array[i].value, array2[i].value);
        }
    }
    
    public void test_1_writer() throws Exception {
        Model[] array = new Model[2048];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new Model();
            array[i].value = Type.A;
        }

        StringWriter writer = new StringWriter();
        JSON.writeJSONString(writer, array);
        String text = writer.toString();

        Model[] array2 = JSON.parseObject(text, Model[].class);

        Assert.assertEquals(array.length, array2.length);
        for (int i = 0; i < array.length; ++i) {
            Assert.assertEquals(array[i].value, array2[i].value);
        }
    }
    
    public void test_null() throws Exception {
        Model[] array = new Model[2048];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new Model();
            array[i].value = null;
        }

        String text = JSON.toJSONString(array, SerializerFeature.WriteMapNullValue);

        Model[] array2 = JSON.parseObject(text, Model[].class);

        Assert.assertEquals(array.length, array2.length);
        for (int i = 0; i < array.length; ++i) {
            Assert.assertEquals(array[i].value, array2[i].value);
        }
    }

    public static class Model {

        public Type value;

    }
    
    public static enum Type {
        A, B, C
    }
}
