package com.alibaba.json.bvt.util;

import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.IOUtils;

import junit.framework.TestCase;

public class ThreadLocalCacheTest extends TestCase {

    public void test() throws Exception {

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.allocateChars(0).length, 1024 * 64);
        Assert.assertEquals(IOUtils.allocateChars(1024).length, 1024 * 64);
        Assert.assertEquals(IOUtils.allocateChars(2048).length, 1024 * 64);
        Assert.assertEquals(IOUtils.allocateChars(0).length, 1024 * 64);
        Assert.assertEquals(IOUtils.allocateChars(1024 * 128).length, 1024 * 128);
        Assert.assertEquals(IOUtils.allocateChars(0).length, 1024 * 64);

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.allocateChars(2048).length, 1024 * 64);

        IOUtils.clearChars();
        Assert.assertEquals(IOUtils.allocateChars(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(IOUtils.allocateChars(0).length, 1024 * 64);
        IOUtils.clearChars();

    }

    public void testBytes() throws Exception {

        clearBytes();
        Assert.assertEquals(getBytes(0).length, 8192);
        Assert.assertEquals(getBytes(1024).length, 8192);
        Assert.assertEquals(getBytes(8192 * 2).length, 8192 * 2);
        Assert.assertEquals(getBytes(0).length, 8192);
        Assert.assertSame(getBytes(0), getBytes(1204));
        Assert.assertNotSame(getBytes(9000), getBytes(9000));

        clearBytes();
        Assert.assertEquals(getBytes(2048).length, 8192);

        clearBytes();
        Assert.assertEquals(getBytes(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(getBytes(0).length, 8192);
        clearBytes();

    }
    
    public static byte[] getBytes(int length) throws Exception {
        Field field = SerializeWriter.class.getDeclaredField("bytesBufLocal");
        field.setAccessible(true);
        ThreadLocal<byte[]> bytesBufLocal = (ThreadLocal<byte[]>) field.get(null);
        
        byte[] bytes = bytesBufLocal.get();

        if (bytes == null) {
            bytes = new byte[1024 * 8];
            bytesBufLocal.set(bytes);
        }
        
        return bytes.length < length //
            ? new byte[length] //
            : bytes;
    }
    
    public static void clearBytes() throws Exception {
        Field field = SerializeWriter.class.getDeclaredField("bytesBufLocal");
        field.setAccessible(true);
        ThreadLocal<byte[]> bytesBufLocal = (ThreadLocal<byte[]>) field.get(null);
        bytesBufLocal.set(null);
    }
}
