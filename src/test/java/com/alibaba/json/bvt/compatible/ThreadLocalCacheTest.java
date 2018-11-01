package com.alibaba.json.bvt.compatible;

import com.alibaba.fastjson.util.ThreadLocalCache;
import junit.framework.TestCase;

/**
 * Created by wenshao on 29/01/2017.
 */
public class ThreadLocalCacheTest extends TestCase{
    public void test_threadCache() throws Exception {
        ThreadLocalCache.getBytes(10);
        ThreadLocalCache.clearBytes();
    }
}
