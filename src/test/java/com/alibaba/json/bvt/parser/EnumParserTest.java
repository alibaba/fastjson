package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;

public class EnumParserTest extends TestCase {

    public void test_0() throws Exception {
        String text = "\"A\"";
        DefaultJSONParser parser = new DefaultJSONParser(text);

        Type type = parser.parseObject(Type.class);
        Assert.assertEquals(Type.A, type);
    }

    public void test_1() throws Exception {
        String text = "0";
        DefaultJSONParser parser = new DefaultJSONParser(text);

        Type type = parser.parseObject(Type.class);
        Assert.assertEquals(Type.A, type);
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            String text = "\"C\"";
            DefaultJSONParser parser = new DefaultJSONParser(text);

            parser.parseObject(Type.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            String text = "4";
            DefaultJSONParser parser = new DefaultJSONParser(text);

            parser.parseObject(Type.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            String text = "4";
            DefaultJSONParser parser = new DefaultJSONParser(text);

            parser.parseObject(TypeA.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            String text = "4";
            DefaultJSONParser parser = new DefaultJSONParser(text);

            new EnumDeserializer(Object.class).deserialze(parser, Object.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            String text = "true";
            DefaultJSONParser parser = new DefaultJSONParser(text);

            new EnumDeserializer(Object.class).deserialze(parser, Object.class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static enum Type {
        A, B
    }

    private static enum TypeA {
        A, B
    }
}
