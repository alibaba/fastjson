package com.alibaba.json.bvt.parser;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

import org.junit.Assert;

import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.UTF8Decoder;

import junit.framework.TestCase;

public class IOUtilsTest extends TestCase {

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            IOUtils.decode(IOUtils.getUTF8Decoder(), ByteBuffer.wrap("abc".getBytes("UTF-8")),
                           CharBuffer.wrap(new char[0]));
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            IOUtils.decode(new MockCharsetDecoder(), ByteBuffer.wrap("abc".getBytes("UTF-8")),
                           CharBuffer.wrap(new char[10]));
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            IOUtils.decode(new MockCharsetDecoder2(), ByteBuffer.wrap("abc".getBytes("UTF-8")),
                           CharBuffer.wrap(new char[10]));
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

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

    public static class MockCharsetDecoder extends UTF8Decoder {

        @Override
        protected CoderResult implFlush(CharBuffer out) {
            return CoderResult.OVERFLOW;
        }
    }

    public static class MockCharsetDecoder2 extends UTF8Decoder {

        @Override
        protected CoderResult implFlush(CharBuffer out) {
            return CoderResult.unmappableForLength(1);
        }
    }
}
