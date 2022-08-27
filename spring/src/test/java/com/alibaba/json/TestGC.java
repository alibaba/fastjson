package com.alibaba.json;

import junit.framework.TestCase;


public class TestGC extends TestCase {
    public void test_0 () throws Exception {
        for (int i = 0; i < 1000 * 1000; ++i) {
            StringBuilder buf = new StringBuilder(1000 * 1000 * 10);
            buf.append(i);
            Thread.sleep(10);
        }
    }
}
