package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;
import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerialWriterStringEncoder;

import junit.framework.TestCase;

public class SerialWriterStringEncoderTest extends TestCase {

    SerialWriterStringEncoder UTF8     = new SerialWriterStringEncoder(Charset.forName("UTF-8"));
    SerialWriterStringEncoder ISO88591 = new SerialWriterStringEncoder(Charset.forName("ISO-8859-1"));
    
    static Field field;
    static {
        try {
            field = SerialWriterStringEncoder.class.getDeclaredField("encoder");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test_0() throws Exception {

        Assert.assertEquals(0, UTF8.encode(new char[0], 0, 0).length);
        Assert.assertEquals(3, UTF8.encode(new char[] { 'a', 'b', 'c' }, 0, 3).length);
    }

//    public void test_error_0() throws Exception {
//        Exception error = null;
//        try {
//            CharsetEncoder encoder = (CharsetEncoder) field.get(UTF8);
//            encoder.reset();
//            UTF8.encode(new char[] { 'a', 'b', 'c' }, 0, 3, new byte[0]);
//        } catch (BufferOverflowException ex) {
//            error = ex;
//        }
//        Assert.assertNotNull(error);
//    }

//    public void test_error_1() throws Exception {
//        Exception error = null;
//        try {
//            CharsetEncoder encoder = (CharsetEncoder) field.get(UTF8);
//            encoder.reset();
//            UTF8.encode(new char[] { '中', '国', '人' }, 0, 3, new byte[8]);
//        } catch (BufferOverflowException ex) {
//            error = ex;
//        }
//        Assert.assertNotNull(error);
//    }

}
