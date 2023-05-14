package com.alibaba.json.bvt.parser;

import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class JSONReaderScannerTest_error extends TestCase {

    public void test_e() throws Exception {
        Exception error = null;
        try {
            new JSONReader(new MyReader());
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class MyReader extends java.io.Reader {

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }

        @Override
        public void close() throws IOException {
            // TODO Auto-generated method stub

        }

    }
}
