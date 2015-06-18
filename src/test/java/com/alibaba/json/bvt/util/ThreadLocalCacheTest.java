package com.alibaba.json.bvt.util;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.util.ThreadLocalCache;

public class ThreadLocalCacheTest extends TestCase {

    public void test() throws Exception {
        new ThreadLocalCache();

        ThreadLocalCache.clearChars();
        Assert.assertEquals(ThreadLocalCache.getChars(0).length, 1024);
        Assert.assertEquals(ThreadLocalCache.getChars(1024).length, 1024);
        Assert.assertEquals(ThreadLocalCache.getChars(2048).length, 2048);
        Assert.assertEquals(ThreadLocalCache.getChars(0).length, 2048);

        ThreadLocalCache.clearChars();
        Assert.assertEquals(ThreadLocalCache.getChars(2048).length, 2048);

        ThreadLocalCache.clearChars();
        Assert.assertEquals(ThreadLocalCache.getChars(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(ThreadLocalCache.getChars(0).length, 1024);
        ThreadLocalCache.clearChars();

    }

    public void testBytes() throws Exception {
        new ThreadLocalCache();

        ThreadLocalCache.clearBytes();
        Assert.assertEquals(ThreadLocalCache.getBytes(0).length, 1024);
        Assert.assertEquals(ThreadLocalCache.getBytes(1024).length, 1024);
        Assert.assertEquals(ThreadLocalCache.getBytes(2048).length, 2048);
        Assert.assertEquals(ThreadLocalCache.getBytes(0).length, 2048);

        ThreadLocalCache.clearBytes();
        Assert.assertEquals(ThreadLocalCache.getBytes(2048).length, 2048);

        ThreadLocalCache.clearBytes();
        Assert.assertEquals(ThreadLocalCache.getBytes(1024 * 256).length, 1024 * 256);
        Assert.assertEquals(ThreadLocalCache.getBytes(0).length, 1024);
        ThreadLocalCache.clearBytes();

    }
}
