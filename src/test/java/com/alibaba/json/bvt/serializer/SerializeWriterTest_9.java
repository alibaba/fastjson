package com.alibaba.json.bvt.serializer;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest_9 extends TestCase {

    public void test_error() throws Exception {
        SerializeWriter writer = new SerializeWriter(new StringWriter());
        Exception error = null;
        try {
            writer.writeTo(new StringWriter());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        writer.close();
    }

    public void test_error_2() throws Exception {
        SerializeWriter writer = new SerializeWriter(new StringWriter());
        Exception error = null;
        try {
            writer.writeTo(new ByteArrayOutputStream(), Charset.forName("UTF-8"));
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        writer.close();
    }
    
    public void test_error_3() throws Exception {
        SerializeWriter writer = new SerializeWriter(new StringWriter());
        Exception error = null;
        try {
            writer.writeTo(new ByteArrayOutputStream(), "UTF-8");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        writer.close();
    }
    
    public void test_error_4() throws Exception {
        SerializeWriter writer = new SerializeWriter(new StringWriter());
        Exception error = null;
        try {
            writer.toCharArray();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        writer.close();
    }
    
    public void test_error_5() throws Exception {
        SerializeWriter writer = new SerializeWriter(new StringWriter());
        Exception error = null;
        try {
            writer.toBytes("UTF-8");
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        writer.close();
    }
}
