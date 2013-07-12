package com.alibaba.json.bvt.parser;

import java.io.Closeable;
import java.io.IOException;

import junit.framework.TestCase;

import com.alibaba.fastjson.util.IOUtils;

public class IOUtilsTest extends TestCase {

    public void test_close() throws Exception {
        IOUtils.close((Closeable) null);
    }

    public void test_close1() throws Exception {
        IOUtils.close(new Closeable() {

            public void close() throws IOException {

            }

        });
    }

    public void test_close_error() throws Exception {
        IOUtils.close(new Closeable() {

            public void close() throws IOException {
                throw new IOException();
            }

        });
    }

}
