package com.alibaba.json.bvt.util;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerialWriterStringEncoder;
import com.alibaba.fastjson.util.IOUtils;

import junit.framework.TestCase;

public class ThreadLocalCacheTest extends TestCase {

    public void test() throws Exception {

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.getChars(0).length, 1024);
        Assert.assertEquals(IOUtils.getChars(1024).length, 1024);
        Assert.assertEquals(IOUtils.getChars(2048).length, 2048);
        Assert.assertEquals(IOUtils.getChars(0).length, 2048);

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.getChars(2048).length, 2048);

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.getChars(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(IOUtils.getChars(0).length, 1024);
        IOUtils.clearChars();

    }

    public void testBytes() throws Exception {

        clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 8192);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(1024).length, 8192);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(8192 * 2).length, 8192 * 2);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 8192);
        Assert.assertSame(SerialWriterStringEncoder.getBytes(0), SerialWriterStringEncoder.getBytes(1204));
        Assert.assertNotSame(SerialWriterStringEncoder.getBytes(9000), SerialWriterStringEncoder.getBytes(9000));

        clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(2048).length, 8192);

        clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 8192);
        clearBytes();

    }
    
    public static void clearBytes() throws Exception {
        Field field = SerialWriterStringEncoder.class.getDeclaredField("bytesBufLocal");
        field.setAccessible(true);
        ThreadLocal<byte[]> bytesBufLocal = (ThreadLocal<byte[]>) field.get(null);
        bytesBufLocal.set(null);
    }
}
