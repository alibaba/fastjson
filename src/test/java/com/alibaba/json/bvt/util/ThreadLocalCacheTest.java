package com.alibaba.json.bvt.util;

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

        SerialWriterStringEncoder.clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 1024);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(1024).length, 1024);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(2048).length, 2048);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 2048);

        SerialWriterStringEncoder.clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(2048).length, 2048);

        SerialWriterStringEncoder.clearBytes();
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(SerialWriterStringEncoder.getBytes(0).length, 1024);
        SerialWriterStringEncoder.clearBytes();

    }
}
