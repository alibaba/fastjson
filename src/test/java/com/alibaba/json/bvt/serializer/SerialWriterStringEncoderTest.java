package com.alibaba.json.bvt.serializer;

import java.nio.charset.Charset;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerialWriterStringEncoder;

public class SerialWriterStringEncoderTest extends TestCase {

    SerialWriterStringEncoder UTF8     = new SerialWriterStringEncoder(Charset.forName("UTF-8"));
    SerialWriterStringEncoder ISO88591 = new SerialWriterStringEncoder(Charset.forName("ISO-8859-1"));

    public void test_0() throws Exception {

        Assert.assertEquals(0, UTF8.encode(new char[0], 0, 0).length);
        Assert.assertEquals(3, UTF8.encode(new char[] { 'a', 'b', 'c' }, 0, 3).length);
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            UTF8.getEncoder().reset();
            UTF8.encode(new char[] { 'a', 'b', 'c' }, 0, 3, new byte[0]);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            UTF8.getEncoder().reset();
            UTF8.encode(new char[] { '中', '国', '人' }, 0, 3, new byte[8]);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

}
